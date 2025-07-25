package com.neulab.fund.service.impl;

import com.neulab.fund.entity.Fund;
import com.neulab.fund.entity.FundCompany;
import com.neulab.fund.entity.FundManager;
import com.neulab.fund.entity.FundTag;
import com.neulab.fund.repository.FundRepository;
import com.neulab.fund.repository.FundCompanyRepository;
import com.neulab.fund.repository.FundManagerRepository;
import com.neulab.fund.repository.FundTagRepository;
import com.neulab.fund.service.FundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

/**
 * 基金服务实现类
 */
@Service
public class FundServiceImpl implements FundService {
    
    @Autowired
    private FundRepository fundRepository;

    @Autowired
    private FundCompanyRepository fundCompanyRepository;
    
    @Autowired
    private FundManagerRepository fundManagerRepository;
    
    @Autowired
    private FundTagRepository fundTagRepository;

    @Override
    public List<Fund> getAllFunds() {
        return fundRepository.findAll();
    }

    @Override
    public Page<Fund> getFundsWithFilter(Map<String, Object> filters, Pageable pageable) {
        Specification<Fund> spec = Specification.where(null);
        if (filters.containsKey("keyword")) {
            String keyword = (String) filters.get("keyword");
            spec = spec.and((root, query, cb) -> cb.or(
                cb.like(root.get("name"), "%" + keyword + "%"),
                cb.like(root.get("code"), "%" + keyword + "%")
            ));
        }
        if (filters.containsKey("companyId")) {
            Long companyId = Long.valueOf(filters.get("companyId").toString());
            spec = spec.and((root, query, cb) -> cb.equal(root.get("companyId"), companyId));
        }
        if (filters.containsKey("managerId")) {
            Long managerId = Long.valueOf(filters.get("managerId").toString());
            spec = spec.and((root, query, cb) -> cb.equal(root.get("managerId"), managerId));
        }
        if (filters.containsKey("type")) {
            String type = (String) filters.get("type");
            spec = spec.and((root, query, cb) -> cb.equal(root.get("type"), type));
        }
        if (filters.containsKey("riskLevel")) {
            String riskLevel = (String) filters.get("riskLevel");
            spec = spec.and((root, query, cb) -> cb.equal(root.get("riskLevel"), riskLevel));
        }
        if (filters.containsKey("tagIds")) {
            @SuppressWarnings("unchecked")
            java.util.List<Long> tagIds = (java.util.List<Long>) filters.get("tagIds");
            spec = spec.and((root, query, cb) -> root.join("tags").get("id").in(tagIds));
        }
        return fundRepository.findAll(spec, pageable);
    }

    @Override
    public Fund getFundById(Long id) {
        return fundRepository.findById(id).orElse(null);
    }

    @Override
    public Fund getFundByCode(String fundCode) {
        return fundRepository.findByCode(fundCode).orElse(null);
    }

    @Override
    public Fund createFund(Fund fund) {
        return fundRepository.save(fund);
    }

    @Override
    public Fund saveFund(Fund fund) {
        return fundRepository.save(fund);
    }

    @Override
    public void deleteFund(Long id) {
        fundRepository.deleteById(id);
    }

    @Override
    public List<FundCompany> getAllFundCompanies() {
        return fundCompanyRepository.findAll();
    }

    @Override
    public FundCompany getFundCompanyById(Long id) {
        return fundCompanyRepository.findById(id).orElse(null);
    }

    @Override
    public List<FundManager> getAllFundManagers() {
        return fundManagerRepository.findAll();
    }

    @Override
    public FundManager getFundManagerById(Long id) {
        return fundManagerRepository.findById(id).orElse(null);
    }

    @Override
    public List<FundTag> getAllFundTags() {
        return fundTagRepository.findAll();
    }

    @Override
    public Map<String, Object> getFundProfile(Long fundId) {
        Fund fund = getFundById(fundId);
        if (fund == null) {
            return null;
        }
        
        Map<String, Object> profile = new HashMap<>();
        profile.put("basicInfo", fund);
        
        // 获取基金公司信息
        if (fund.getCompanyId() != null) {
            FundCompany company = getFundCompanyById(fund.getCompanyId());
            profile.put("company", company);
        }
        
        // 获取基金经理信息
        if (fund.getManagerId() != null) {
            FundManager manager = getFundManagerById(fund.getManagerId());
            profile.put("manager", manager);
        }
        
        // 计算基金画像指标
        Map<String, Object> metrics = calculateFundMetrics(fund);
        profile.put("metrics", metrics);
        
        return profile;
    }
    
    /**
     * 计算基金画像指标
     */
    private Map<String, Object> calculateFundMetrics(Fund fund) {
        Map<String, Object> metrics = new HashMap<>();
        
        // 这里可以添加复杂的基金分析指标计算
        // 例如：夏普比率、最大回撤、年化收益率等
        
        metrics.put("riskLevel", fund.getStatus());
        metrics.put("fundType", fund.getType());
        metrics.put("fundCode", fund.getCode());
        
        return metrics;
    }

    @Override
    public List<com.neulab.fund.vo.FundListVO> getAllFundVOs() {
        List<Fund> fundList = fundRepository.findAll();
        List<com.neulab.fund.vo.FundListVO> voList = new java.util.ArrayList<>();
        for (Fund fund : fundList) {
            voList.add(toFundListVO(fund));
        }
        return voList;
    }

    @Override
    public org.springframework.data.domain.Page<com.neulab.fund.vo.FundListVO> getFundVOsWithFilter(Map<String, Object> filters, org.springframework.data.domain.Pageable pageable) {
        org.springframework.data.domain.Page<Fund> page = getFundsWithFilter(filters, pageable);
        return page.map(this::toFundListVO);
    }

    private com.neulab.fund.vo.FundListVO toFundListVO(Fund fund) {
        com.neulab.fund.vo.FundListVO vo = new com.neulab.fund.vo.FundListVO();
        vo.setId(fund.getId());
        vo.setFundCode(fund.getFundCode());
        vo.setFundName(fund.getFundName());
        vo.setCompanyId(fund.getCompanyId());
        if (fund.getCompanyId() != null) {
            FundCompany company = fundCompanyRepository.findById(fund.getCompanyId()).orElse(null);
            vo.setCompanyName(company != null ? company.getCompanyName() : null);
        }
        vo.setManagerId(fund.getManagerId());
        if (fund.getManagerId() != null) {
            FundManager manager = fundManagerRepository.findById(fund.getManagerId()).orElse(null);
            vo.setManagerName(manager != null ? manager.getManagerName() : null);
        }
        vo.setInceptionDate(fund.getInceptionDate());
        vo.setFundSize(fund.getFundSize());
        vo.setNav(fund.getNav());
        vo.setNavDate(fund.getNavDate());
        vo.setRiskLevel(fund.getRiskLevel());
        vo.setInvestmentStrategy(fund.getInvestmentStrategy());
        vo.setBenchmark(fund.getBenchmark());
        vo.setStatus(fund.getStatus());
        vo.setCode(fund.getCode());
        vo.setName(fund.getName());
        vo.setType(fund.getType());
        vo.setCreatedAt(fund.getCreatedAt());
        vo.setUpdatedAt(fund.getUpdatedAt());
        // tags转换
        if (fund.getTags() != null) {
            java.util.List<com.neulab.fund.vo.TagVO> tagVOList = new java.util.ArrayList<>();
            for (com.neulab.fund.entity.FundTag tag : fund.getTags()) {
                com.neulab.fund.vo.TagVO tagVO = new com.neulab.fund.vo.TagVO();
                tagVO.setId(tag.getId());
                tagVO.setTagName(tag.getTagName());
                tagVO.setCreatedAt(tag.getCreatedAt());
                tagVO.setUpdatedAt(tag.getUpdatedAt());
                tagVOList.add(tagVO);
            }
            vo.setTags(tagVOList);
        } else {
            vo.setTags(null);
        }
        return vo;
    }
} 