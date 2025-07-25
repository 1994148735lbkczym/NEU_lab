package com.neulab.fund.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 策略实体
 */
@Entity
@Table(name = "`strategy`")
public class Strategy {
    /** 主键ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 策略代码 */
    @Column(name = "strategy_code", nullable = false, unique = true)
    private String strategyCode;
    /** 策略名称 */
    @Column(name = "strategy_name", nullable = false)
    private String strategyName;
    /** 策略类型 */
    @Column(name = "strategy_type")
    private String strategyType;
    /** 风险等级 */
    @Column(name = "risk_level")
    private String riskLevel;
    /** 目标收益率 */
    @Column(name = "target_return")
    private Double targetReturn;
    /** 最大回撤 */
    @Column(name = "max_drawdown")
    private Double maxDrawdown;
    /** 投资期限 */
    @Column(name = "investment_horizon")
    private String investmentHorizon;
    /** 策略描述 */
    @Column(name = "description")
    private String description;
    /** 关联因子树ID */
    @Column(name = "factor_tree_id")
    private Long factorTreeId;
    /** 策略参数JSON */
    @Lob
    @Column(name = "parameters")
    private String parameters;
    /** 状态 */
    @Column(name = "status")
    private String status;
    /** 创建时间 */
    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
    /** 更新时间 */
    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;

    // getter/setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getStrategyCode() { return strategyCode; }
    public void setStrategyCode(String strategyCode) { this.strategyCode = strategyCode; }
    
    public String getStrategyName() { return strategyName; }
    public void setStrategyName(String strategyName) { this.strategyName = strategyName; }
    
    public String getStrategyType() { return strategyType; }
    public void setStrategyType(String strategyType) { this.strategyType = strategyType; }
    
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    
    public Double getTargetReturn() { return targetReturn; }
    public void setTargetReturn(Double targetReturn) { this.targetReturn = targetReturn; }
    
    public Double getMaxDrawdown() { return maxDrawdown; }
    public void setMaxDrawdown(Double maxDrawdown) { this.maxDrawdown = maxDrawdown; }
    
    public String getInvestmentHorizon() { return investmentHorizon; }
    public void setInvestmentHorizon(String investmentHorizon) { this.investmentHorizon = investmentHorizon; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Long getFactorTreeId() { return factorTreeId; }
    public void setFactorTreeId(Long factorTreeId) { this.factorTreeId = factorTreeId; }
    
    public String getParameters() { return parameters; }
    public void setParameters(String parameters) { this.parameters = parameters; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
} 