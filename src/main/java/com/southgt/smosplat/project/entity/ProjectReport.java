package com.southgt.smosplat.project.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "project_report")
public class ProjectReport implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	@Column(name = "report_uuid", length = 50, nullable = false, unique = true, updatable = false)
	private String reportUuid;

	/**
	 * 编号
	 */
	@Column(name = "p1_p1", length = 100)
	private String p1p1;

	/**
	 * 工程名称
	 */
	@Column(name = "p1_p2", length = 100)
	private String p1p2;

	/**
	 * 工程地址
	 */
	@Column(name = "p1_p3", length = 100)
	private String p1p3;

	/**
	 * 委托单位
	 */
	@Column(name = "p1_p4", length = 100)
	private String p1p4;

	/**
	 * 监测起始时间
	 */
	@Column(name = "p1_p5", length = 100)
	private String p1p5;

	/**
	 * 监测终止时间
	 */
	@Column(name = "p1_p6", length = 100)
	private String p1p6;

	/**
	 * 围护墙(边坡)顶部水平位移观测仪器
	 */
	@Column(name = "p1_p78", length = 100)
	private String p1p78;

	/**
	 * 围护墙(边坡)顶部水平位移依据规范
	 */
	@Column(name = "p1_p79", length = 100)
	private String p1p79;

	/**
	 * 围护墙(边坡)顶部竖向位移观测仪器
	 */
	@Column(name = "p1_p80", length = 100)
	private String p1p80;

	/**
	 * 围护墙(边坡)顶部竖向位移依据规范
	 */
	@Column(name = "p1_p81", length = 100)
	private String p1p81;

	/**
	 * 锚杆内力观测仪器
	 */
	@Column(name = "p1_p82", length = 100)
	private String p1p82;

	/**
	 * 锚杆内力依据规范
	 */
	@Column(name = "p1_p83", length = 100)
	private String p1p83;

	/**
	 * 周边建筑物竖向位移观测仪器
	 */
	@Column(name = "p1_p84", length = 100)
	private String p1p84;

	/**
	 * 周边建筑物竖向位移依据规范
	 */
	@Column(name = "p1_p85", length = 100)
	private String p1p85;

	/**
	 * 周边管线竖向位移观测仪器
	 */
	@Column(name = "p1_p86", length = 100)
	private String p1p86;

	/**
	 * 周边管线竖向位移依据规范
	 */
	@Column(name = "p1_p87", length = 100)
	private String p1p87;

	/**
	 * 地下水位观测仪器
	 */
	@Column(name = "p1_p88", length = 100)
	private String p1p88;

	/**
	 * 地下水位依据规范
	 */
	@Column(name = "p1_89", length = 100)
	private String p1p89;

	/**
	 * 支护结构深层水平位移观测仪器
	 */
	@Column(name = "p1_p90", length = 100)
	private String p1p90;

	/**
	 * 支护结构深层水平位移依据规范
	 */
	@Column(name = "p1_p91", length = 100)
	private String p1p91;

	/**
	 * 立柱竖向位移观测仪器
	 */
	@Column(name = "p1_p92", length = 100)
	private String p1p92;

	/**
	 * 立柱竖向位移依据规范
	 */
	@Column(name = "p1_p93", length = 100)
	private String p1p93;

	/**
	 * 支撑内力观测仪器
	 */
	@Column(name = "p1_p94", length = 100)
	private String p1p94;

	/**
	 * 支撑内力依据规范
	 */
	@Column(name = "p1_p95", length = 100)
	private String p1p95;

	/**
	 * 编号
	 */
	@Column(name = "p2_p1", length = 100)
	private String p2p1;

	/**
	 * 检测单位地址
	 */
	@Column(name = "p2_p2", length = 100)
	private String p2p2;

	/**
	 * 邮政编码
	 */
	@Column(name = "p2_p3", length = 100)
	private String p2p3;

	/**
	 * 联系电话
	 */
	@Column(name = "p2_p4", length = 100)
	private String p2p4;

	/**
	 * 联系人
	 */
	@Column(name = "p2_p5", length = 100)
	private String p2p5;

	/**
	 * 重要提示
	 */
	@Column(name = "p2_p6", length = 500)
	private String p2p6;

	/**
	 * 工程名称
	 */
	@Column(name = "p4_p1", length = 100)
	private String p4p1;

	/**
	 * 工程地址
	 */
	@Column(name = "p4_p2", length = 100)
	private String p4p2;

	/**
	 * 建设单位
	 */
	@Column(name = "p4_p3", length = 100)
	private String p4p3;

	/**
	 * 勘察单位
	 */
	@Column(name = "p4_p4", length = 100)
	private String p4p4;

	/**
	 * 设计单位
	 */
	@Column(name = "p4_p5", length = 100)
	private String p4p5;

	/**
	 * 施工单位
	 */
	@Column(name = "p4_p6", length = 100)
	private String p4p6;

	/**
	 * 监理单位
	 */
	@Column(name = "p4_p7", length = 100)
	private String p4p7;

	/**
	 * 项目用地面积
	 */
	@Column(name = "p4_p8", length = 100)
	private String p4p8;

	/**
	 * 基坑面积
	 */
	@Column(name = "p4_p9", length = 100)
	private String p4p9;

	/**
	 * 基坑开挖深度
	 */
	@Column(name = "p4_p10", length = 100)
	private String p4p10;

	/**
	 * 基坑安全等级
	 */
	@Column(name = "p4_p11", length = 100)
	private String p4p11;

	/**
	 * 基坑开挖周长
	 */
	@Column(name = "p4_p12", length = 100)
	private String p4p12;

	/**
	 * 基坑开挖面积
	 */
	@Column(name = "p4_p13", length = 100)
	private String p4p13;

	/**
	 * 基坑监测日期
	 */
	@Column(name = "p4_p14", length = 100)
	private String p4p14;

	/**
	 * 基坑监测期数
	 */
	@Column(name = "p4_p15", length = 100)
	private String p4p15;

	/**
	 * 本期工况
	 */
	@Column(name = "p4_p16", length = 100)
	private String p4p16;

	/**
	 * 监测目的
	 */
	@Column(name = "p4_p17", length = 100)
	private String p4p17;

	/**
	 * 备 注
	 */
	@Column(name = "p4_p18", length = 100)
	private String p4p18;

	/**
	 * 水准仪规格型号
	 */
	@Column(name = "p5_p1", length = 100)
	private String p5p1;

	/**
	 * 水准仪编号
	 */
	@Column(name = "p5_p2", length = 100)
	private String p5p2;

	/**
	 * 水准仪校准证号
	 */
	@Column(name = "p5_p3", length = 100)
	private String p5p3;

	/**
	 * 水准仪有效日期
	 */
	@Column(name = "p5_p4", length = 100)
	private String p5p4;

	/**
	 * 因瓦条码水准标尺规格型号
	 */
	@Column(name = "p5_p5", length = 100)
	private String p5p5;

	/**
	 * 因瓦条码水准标尺编号
	 */
	@Column(name = "p5_p6", length = 100)
	private String p5p6;

	/**
	 * 因瓦条码水准标尺校准证号
	 */
	@Column(name = "p5_p7", length = 100)
	private String p5p7;

	/**
	 * 因瓦条码水准标尺有效日期
	 */
	@Column(name = "p5_p8", length = 100)
	private String p5p8;

	/**
	 * 振弦读数仪规格型号
	 */
	@Column(name = "p5_p9", length = 100)
	private String p5p9;

	/**
	 * 振弦读数仪编号
	 */
	@Column(name = "p5_p10", length = 100)
	private String p5p10;

	/**
	 * 振弦读数仪校准证号
	 */
	@Column(name = "p5_p11", length = 100)
	private String p5p11;

	/**
	 * 振弦读数仪有效日期
	 */
	@Column(name = "p5_p12", length = 100)
	private String p5p12;

	/**
	 * 振弦水位计规格型号
	 */
	@Column(name = "p5_p13", length = 100)
	private String p5p13;

	/**
	 * 振弦水位计编号
	 */
	@Column(name = "p5_p14", length = 100)
	private String p5p14;

	/**
	 * 振弦水位计校准证号
	 */
	@Column(name = "p5_p15", length = 100)
	private String p5p15;

	/**
	 * 振弦水位计有效日期
	 */
	@Column(name = "p5_p16", length = 100)
	private String p5p16;

	/**
	 * 全站仪规格型号
	 */
	@Column(name = "p5_p17", length = 100)
	private String p5p17;

	/**
	 * 全站仪编号
	 */
	@Column(name = "p5_p18", length = 100)
	private String p5p18;

	/**
	 * 全站仪校准证号
	 */
	@Column(name = "p5_p19", length = 100)
	private String p5p19;

	/**
	 * 全站仪有效日期
	 */
	@Column(name = "p5_p20", length = 100)
	private String p5p20;

	/**
	 * 测斜仪规格型号
	 */
	@Column(name = "p5_p21", length = 100)
	private String p5p21;

	/**
	 * 测斜仪编号
	 */
	@Column(name = "p5_p22", length = 100)
	private String p5p22;

	/**
	 * 测斜仪校准证号
	 */
	@Column(name = "p5_p23", length = 100)
	private String p5p23;

	/**
	 * 测斜仪有效日期
	 */
	@Column(name = "p5_p24", length = 100)
	private String p5p24;

	/**
	 * 检测标准
	 */
	@Column(name = "p5_p25", length = 500)
	private String p5p25;

	/**
	 * 围护墙(边坡)顶部水平位移合同监测点数
	 */
	@Column(name = "p6_p1", length = 100)
	private Double p6p1;

	/**
	 * 围护墙(边坡)顶部水平位移已设置监测点
	 */
	@Column(name = "p6_p2", length = 100)
	private Double p6p2;

	/**
	 * 围护墙(边坡)顶部水平位移破坏监测点
	 */
	@Column(name = "p6_p3", length = 100)
	private Double p6p3;

	/**
	 * 围护墙(边坡)顶部水平位移剩余监测点
	 */
	@Column(name = "p6_p4", length = 100)
	private Double p6p4;

	/**
	 * 围护墙(边坡)顶部水平位移本期完成量
	 */
	@Column(name = "p6_p5", length = 100)
	private Double p6p5;

	/**
	 * 围护墙(边坡)顶部水平位移累计完成量
	 */
	@Column(name = "p6_p6", length = 100)
	private Double p6p6;

	/**
	 * 围护墙(边坡)顶部水平位移备注
	 */
	@Column(name = "p6_p7", length = 100)
	private String p6p7;

	/**
	 * 围护墙(边坡)顶部竖向位移合同监测点数
	 */
	@Column(name = "p6_p8", length = 100)
	private Double p6p8;

	/**
	 * 围护墙(边坡)顶部竖向位移已设置监测点
	 */
	@Column(name = "p6_p9", length = 100)
	private Double p6p9;

	/**
	 * 围护墙(边坡)顶部竖向位移破坏监测点
	 */
	@Column(name = "p6_p10", length = 100)
	private Double p6p10;

	/**
	 * 围护墙(边坡)顶部竖向位移剩余监测点
	 */
	@Column(name = "p6_p11", length = 100)
	private Double p6p11;

	/**
	 * 围护墙(边坡)顶部竖向位移本期完成量
	 */
	@Column(name = "p6_p12", length = 100)
	private Double p6p12;

	/**
	 * 围护墙(边坡)顶部竖向位移累计完成量
	 */
	@Column(name = "p6_p13", length = 100)
	private Double p6p13;

	/**
	 * 围护墙(边坡)顶部竖向位移备注
	 */
	@Column(name = "p6_p14", length = 100)
	private String p6p14;

	/**
	 * 周边建筑物竖向位移合同监测点数
	 */
	@Column(name = "p6_p15", length = 100)
	private Double p6p15;

	/**
	 * 周边建筑物竖向位移已设置监测点
	 */
	@Column(name = "p6_p16", length = 100)
	private Double p6p16;

	/**
	 * 周边建筑物竖向位移破坏监测点
	 */
	@Column(name = "p6_p17", length = 100)
	private Double p6p17;

	/**
	 * 周边建筑物竖向位移剩余监测点
	 */
	@Column(name = "p6_p18", length = 100)
	private Double p6p18;

	/**
	 * 周边建筑物竖向位移本期完成量
	 */
	@Column(name = "p6_p19", length = 100)
	private Double p6p19;

	/**
	 * 周边建筑物竖向位移累计完成量
	 */
	@Column(name = "p6_p20", length = 100)
	private Double p6p20;

	/**
	 * 周边建筑物竖向位移备注
	 */
	@Column(name = "p6_p21", length = 100)
	private String p6p21;

	/**
	 * 周边管线竖向位移合同监测点数
	 */
	@Column(name = "p6_p22", length = 100)
	private Double p6p22;

	/**
	 * 周边管线竖向位移已设置监测点
	 */
	@Column(name = "p6_p23", length = 100)
	private Double p6p23;

	/**
	 * 周边管线竖向位移破坏监测点
	 */
	@Column(name = "p6_p24", length = 100)
	private Double p6p24;

	/**
	 * 周边管线竖向位移剩余监测点
	 */
	@Column(name = "p6_p25", length = 100)
	private Double p6p25;

	/**
	 * 周边管线竖向位移本期完成量
	 */
	@Column(name = "p6_p26", length = 100)
	private Double p6p26;

	/**
	 * 周边管线竖向位移累计完成量
	 */
	@Column(name = "p6_p27", length = 100)
	private Double p6p27;

	/**
	 * 周边管线竖向位移备注
	 */
	@Column(name = "p6_p28", length = 100)
	private String p6p28;

	/**
	 * 支护结构深层水平位移合同监测点数
	 */
	@Column(name = "p6_p29", length = 100)
	private Double p6p29;

	/**
	 * 支护结构深层水平位移已设置监测点
	 */
	@Column(name = "p6_p30", length = 100)
	private Double p6p30;

	/**
	 * 支护结构深层水平位移破坏监测dian
	 */
	@Column(name = "p6_p31", length = 100)
	private Double p6p31;

	/**
	 * 支护结构深层水平位移剩余监测点
	 */
	@Column(name = "p6_p32", length = 100)
	private Double p6p32;

	/**
	 * 支护结构深层水平位移本期完成量
	 */
	@Column(name = "p6_p33", length = 100)
	private Double p6p33;

	/**
	 * 支护结构深层水平位移累计完成量
	 */
	@Column(name = "p6_p34", length = 100)
	private Double p6p34;

	/**
	 * 支护结构深层水平位移备注
	 */
	@Column(name = "p6_p35", length = 100)
	private Double p6p35;

	/**
	 * 锚索内力监测点合同监测点数
	 */
	@Column(name = "p6_p36", length = 100)
	private Double p6p36;

	/**
	 * 锚索内力监测点已设置监测点
	 */
	@Column(name = "p6_p37", length = 100)
	private Double p6p37;

	/**
	 * 锚索内力监测点破坏监测点
	 */
	@Column(name = "p6_p38", length = 100)
	private Double p6p38;

	/**
	 * 锚索内力监测点剩余监测点
	 */
	@Column(name = "p6_p39", length = 100)
	private Double p6p39;

	/**
	 * 锚索内力监测点本期完成量
	 */
	@Column(name = "p6_p40", length = 100)
	private Double p6p40;

	/**
	 * 锚索内力监测点累计完成量
	 */
	@Column(name = "p6_p41", length = 100)
	private Double p6p41;

	/**
	 * 锚索内力监测点备注
	 */
	@Column(name = "p6_p42", length = 100)
	private String p6p42;

	/**
	 * 地下水位监测点合同监测点数
	 */
	@Column(name = "p6_p43", length = 100)
	private Double p6p43;

	/**
	 * 地下水位监测点已设置监测点
	 */
	@Column(name = "p6_p44", length = 100)
	private Double p6p44;

	/**
	 * 地下水位监测点破坏监测点
	 */
	@Column(name = "p6_p45", length = 100)
	private Double p6p45;

	/**
	 * 地下水位监测点剩余监测点
	 */
	@Column(name = "p6_p46", length = 100)
	private Double p6p46;

	/**
	 * 地下水位监测点本期完成量
	 */
	@Column(name = "p6_p47", length = 100)
	private Double p6p47;

	/**
	 * 地下水位监测点累计完成量
	 */
	@Column(name = "p6_p48", length = 100)
	private Double p6p48;

	/**
	 * 地下水位监测点备注
	 */
	@Column(name = "p6_p49", length = 100)
	private String p6p49;

	/**
	 * 立柱竖向位移监测点合同监测点数
	 */
	@Column(name = "p6_p50", length = 100)
	private Double p6p50;

	/**
	 * 立柱竖向位移监测点已设置监测点
	 */
	@Column(name = "p6_p51", length = 100)
	private Double p6p51;

	/**
	 * 立柱竖向位移监测点破坏监测点
	 */
	@Column(name = "p6_p52", length = 100)
	private Double p6p52;

	/**
	 * 立柱竖向位移监测点剩余监测点
	 */
	@Column(name = "p6_p53", length = 100)
	private Double p6p53;

	/**
	 * 立柱竖向位移监测点本期完成量
	 */
	@Column(name = "p6_p54", length = 100)
	private Double p6p54;

	/**
	 * 立柱竖向位移监测点累计完成量
	 */
	@Column(name = "p6_p55", length = 100)
	private Double p6p55;

	/**
	 * 立柱竖向位移监测点备注
	 */
	@Column(name = "p6_p56", length = 100)
	private String p6p56;
	/**
	 * 支撑内力监测点合同监测点数
	 */
	@Column(name = "p6_p57", length = 100)
	private Double p6p57;

	/**
	 * 支撑内力监测点已设置监测点
	 */
	@Column(name = "p6_p58", length = 100)
	private Double p6p58;

	/**
	 * 支撑内力监测点破坏监测点
	 */
	@Column(name = "p6_p59", length = 100)
	private Double p6p59;

	/**
	 * 支撑内力监测点剩余监测点
	 */
	@Column(name = "p6_p60", length = 100)
	private Double p6p60;

	/**
	 * 支撑内力监测点本期完成量
	 */
	@Column(name = "p6_p61", length = 100)
	private Double p6p61;

	/**
	 * 支撑内力监测点累计完成量
	 */
	@Column(name = "p6_p62", length = 100)
	private Double p6p62;

	/**
	 * 支撑内力监测点备注
	 */
	@Column(name = "p6_p63", length = 100)
	private String p6p63;

	/**
	 * 合同监测数量
	 */
	@Column(name = "p6_p64", length = 50)
	private String p6p64;

	/**
	 * 已完成监测数量
	 */
	@Column(name = "p6_p65", length = 50)
	private String p6p65;

	/**
	 * 本期完成_次
	 */
	@Column(name = "p6_p66", length = 50)
	private String p6p66;

	/**
	 * 围护墙(边坡)顶部水平位移备注
	 */
	@Column(name = "p7_p8", length = 100)
	private String p7p8;

	/**
	 * 围护墙(边坡)顶部竖向位移备注
	 */
	@Column(name = "p7_p16", length = 100)
	private String p7p16;

	/**
	 * 锚杆内力备注
	 */
	@Column(name = "p7_p24", length = 100)
	private String p7p24;

	/**
	 * 周边建筑物竖向位移内力备注
	 */
	@Column(name = "p7_p32", length = 100)
	private String p7p32;

	/**
	 * 周边管线竖向位移备注
	 */
	@Column(name = "p7_p40", length = 100)
	private String p7p40;

	/**
	 * 地下水位备注
	 */
	@Column(name = "p7_p48", length = 100)
	private String p7p48;

	/**
	 * 支护结构深层水平位移备注
	 */
	@Column(name = "p7_p56", length = 100)
	private String p7p56;

	/**
	 * 立柱备注
	 */
	@Column(name = "p7_p64", length = 100)
	private String p7p64;

	/**
	 * 支撑内力位移备注
	 */
	@Column(name = "p7_p72", length = 100)
	private String p7p72;

	/**
	 * 分析及建议
	 */
	@Column(name = "p8_p1", length = 5000)
	private String p8p1;

	/**
	 * 主要监测人
	 */
	@Column(name = "p8_p2", length = 100)
	private String p8p2;

	/**
	 * 上岗证号1
	 * 
	 */
	@Column(name = "p8_p3", length = 100)
	private String p8p3;

	/**
	 * 主要监测人2
	 */
	@Column(name = "p8_p4", length = 100)
	private String p8p4;

	/**
	 * 上岗证号2
	 */
	@Column(name = "p8_p5", length = 100)
	private String p8p5;

	/**
	 * 报告编写人
	 */
	@Column(name = "p8_p6", length = 100)
	private String p8p6;

	/**
	 * 上岗证号3
	 */
	@Column(name = "p8_p7", length = 100)
	private String p8p7;

	/**
	 * 报告审核人
	 */
	@Column(name = "p8_p8", length = 100)
	private String p8p8;

	/**
	 * 上岗证号4
	 */
	@Column(name = "p8_p9", length = 100)
	private String p8p9;

	/**
	 * 报告批准人
	 */
	@Column(name = "p8_p10", length = 100)
	private String p8p10;

	/**
	 * 职务
	 */
	@Column(name = "p8_p11", length = 100)
	private String p8p11;

	/**
	 * 所属项目
	 */
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "project_uuid")
	private Project project;

	public String getReportUuid() {
		return reportUuid;
	}

	public void setReportUuid(String reportUuid) {
		this.reportUuid = reportUuid;
	}

	public String getP1p1() {
		return p1p1;
	}

	public void setP1p1(String p1p1) {
		this.p1p1 = p1p1;
	}

	public String getP1p2() {
		return p1p2;
	}

	public void setP1p2(String p1p2) {
		this.p1p2 = p1p2;
	}

	public String getP1p3() {
		return p1p3;
	}

	public void setP1p3(String p1p3) {
		this.p1p3 = p1p3;
	}

	public String getP1p4() {
		return p1p4;
	}

	public void setP1p4(String p1p4) {
		this.p1p4 = p1p4;
	}

	public String getP1p5() {
		return p1p5;
	}

	public void setP1p5(String p1p5) {
		this.p1p5 = p1p5;
	}

	public String getP1p6() {
		return p1p6;
	}

	public void setP1p6(String p1p6) {
		this.p1p6 = p1p6;
	}

	public String getP1p78() {
		return p1p78;
	}

	public void setP1p78(String p1p78) {
		this.p1p78 = p1p78;
	}

	public String getP1p79() {
		return p1p79;
	}

	public void setP1p79(String p1p79) {
		this.p1p79 = p1p79;
	}

	public String getP1p80() {
		return p1p80;
	}

	public void setP1p80(String p1p80) {
		this.p1p80 = p1p80;
	}

	public String getP1p81() {
		return p1p81;
	}

	public void setP1p81(String p1p81) {
		this.p1p81 = p1p81;
	}

	public String getP1p82() {
		return p1p82;
	}

	public void setP1p82(String p1p82) {
		this.p1p82 = p1p82;
	}

	public String getP1p83() {
		return p1p83;
	}

	public void setP1p83(String p1p83) {
		this.p1p83 = p1p83;
	}

	public String getP1p84() {
		return p1p84;
	}

	public void setP1p84(String p1p84) {
		this.p1p84 = p1p84;
	}

	public String getP1p85() {
		return p1p85;
	}

	public void setP1p85(String p1p85) {
		this.p1p85 = p1p85;
	}

	public String getP1p86() {
		return p1p86;
	}

	public void setP1p86(String p1p86) {
		this.p1p86 = p1p86;
	}

	public String getP1p87() {
		return p1p87;
	}

	public void setP1p87(String p1p87) {
		this.p1p87 = p1p87;
	}

	public String getP1p88() {
		return p1p88;
	}

	public void setP1p88(String p1p88) {
		this.p1p88 = p1p88;
	}

	public String getP1p89() {
		return p1p89;
	}

	public void setP1p89(String p1p89) {
		this.p1p89 = p1p89;
	}

	public String getP1p90() {
		return p1p90;
	}

	public void setP1p90(String p1p90) {
		this.p1p90 = p1p90;
	}

	public String getP1p91() {
		return p1p91;
	}

	public void setP1p91(String p1p91) {
		this.p1p91 = p1p91;
	}

	public String getP1p92() {
		return p1p92;
	}

	public void setP1p92(String p1p92) {
		this.p1p92 = p1p92;
	}

	public String getP1p93() {
		return p1p93;
	}

	public void setP1p93(String p1p93) {
		this.p1p93 = p1p93;
	}

	public String getP1p94() {
		return p1p94;
	}

	public void setP1p94(String p1p94) {
		this.p1p94 = p1p94;
	}

	public String getP1p95() {
		return p1p95;
	}

	public void setP1p95(String p1p95) {
		this.p1p95 = p1p95;
	}

	public String getP2p1() {
		return p2p1;
	}

	public void setP2p1(String p2p1) {
		this.p2p1 = p2p1;
	}

	public String getP2p2() {
		return p2p2;
	}

	public void setP2p2(String p2p2) {
		this.p2p2 = p2p2;
	}

	public String getP2p3() {
		return p2p3;
	}

	public void setP2p3(String p2p3) {
		this.p2p3 = p2p3;
	}

	public String getP2p4() {
		return p2p4;
	}

	public void setP2p4(String p2p4) {
		this.p2p4 = p2p4;
	}

	public String getP2p5() {
		return p2p5;
	}

	public void setP2p5(String p2p5) {
		this.p2p5 = p2p5;
	}

	public String getP4p1() {
		return p4p1;
	}

	public void setP4p1(String p4p1) {
		this.p4p1 = p4p1;
	}

	public String getP4p2() {
		return p4p2;
	}

	public void setP4p2(String p4p2) {
		this.p4p2 = p4p2;
	}

	public String getP4p3() {
		return p4p3;
	}

	public void setP4p3(String p4p3) {
		this.p4p3 = p4p3;
	}

	public String getP4p4() {
		return p4p4;
	}

	public void setP4p4(String p4p4) {
		this.p4p4 = p4p4;
	}

	public String getP4p5() {
		return p4p5;
	}

	public void setP4p5(String p4p5) {
		this.p4p5 = p4p5;
	}

	public String getP4p6() {
		return p4p6;
	}

	public void setP4p6(String p4p6) {
		this.p4p6 = p4p6;
	}

	public String getP4p7() {
		return p4p7;
	}

	public void setP4p7(String p4p7) {
		this.p4p7 = p4p7;
	}

	public String getP4p11() {
		return p4p11;
	}

	public void setP4p11(String p4p11) {
		this.p4p11 = p4p11;
	}

	public String getP4p8() {
		return p4p8;
	}

	public void setP4p8(String p4p8) {
		this.p4p8 = p4p8;
	}

	public String getP4p9() {
		return p4p9;
	}

	public void setP4p9(String p4p9) {
		this.p4p9 = p4p9;
	}

	public String getP4p10() {
		return p4p10;
	}

	public void setP4p10(String p4p10) {
		this.p4p10 = p4p10;
	}

	public String getP4p12() {
		return p4p12;
	}

	public void setP4p12(String p4p12) {
		this.p4p12 = p4p12;
	}

	public String getP4p13() {
		return p4p13;
	}

	public void setP4p13(String p4p13) {
		this.p4p13 = p4p13;
	}

	public String getP4p14() {
		return p4p14;
	}

	public void setP4p14(String p4p14) {
		this.p4p14 = p4p14;
	}

	public String getP4p15() {
		return p4p15;
	}

	public void setP4p15(String p4p15) {
		this.p4p15 = p4p15;
	}

	public String getP4p16() {
		return p4p16;
	}

	public void setP4p16(String p4p16) {
		this.p4p16 = p4p16;
	}

	public String getP4p17() {
		return p4p17;
	}

	public void setP4p17(String p4p17) {
		this.p4p17 = p4p17;
	}

	public String getP4p18() {
		return p4p18;
	}

	public void setP4p18(String p4p18) {
		this.p4p18 = p4p18;
	}

	public String getP5p1() {
		return p5p1;
	}

	public void setP5p1(String p5p1) {
		this.p5p1 = p5p1;
	}

	public String getP5p2() {
		return p5p2;
	}

	public void setP5p2(String p5p2) {
		this.p5p2 = p5p2;
	}

	public String getP5p3() {
		return p5p3;
	}

	public void setP5p3(String p5p3) {
		this.p5p3 = p5p3;
	}

	public String getP5p4() {
		return p5p4;
	}

	public void setP5p4(String p5p4) {
		this.p5p4 = p5p4;
	}

	public String getP5p5() {
		return p5p5;
	}

	public void setP5p5(String p5p5) {
		this.p5p5 = p5p5;
	}

	public String getP5p6() {
		return p5p6;
	}

	public void setP5p6(String p5p6) {
		this.p5p6 = p5p6;
	}

	public String getP5p7() {
		return p5p7;
	}

	public void setP5p7(String p5p7) {
		this.p5p7 = p5p7;
	}

	public String getP5p8() {
		return p5p8;
	}

	public void setP5p8(String p5p8) {
		this.p5p8 = p5p8;
	}

	public String getP5p9() {
		return p5p9;
	}

	public void setP5p9(String p5p9) {
		this.p5p9 = p5p9;
	}

	public String getP5p10() {
		return p5p10;
	}

	public void setP5p10(String p5p10) {
		this.p5p10 = p5p10;
	}

	public String getP5p11() {
		return p5p11;
	}

	public void setP5p11(String p5p11) {
		this.p5p11 = p5p11;
	}

	public String getP5p12() {
		return p5p12;
	}

	public void setP5p12(String p5p12) {
		this.p5p12 = p5p12;
	}

	public String getP5p13() {
		return p5p13;
	}

	public void setP5p13(String p5p13) {
		this.p5p13 = p5p13;
	}

	public String getP5p14() {
		return p5p14;
	}

	public void setP5p14(String p5p14) {
		this.p5p14 = p5p14;
	}

	public String getP5p15() {
		return p5p15;
	}

	public void setP5p15(String p5p15) {
		this.p5p15 = p5p15;
	}

	public String getP5p16() {
		return p5p16;
	}

	public void setP5p16(String p5p16) {
		this.p5p16 = p5p16;
	}

	public String getP5p17() {
		return p5p17;
	}

	public void setP5p17(String p5p17) {
		this.p5p17 = p5p17;
	}

	public String getP5p18() {
		return p5p18;
	}

	public void setP5p18(String p5p18) {
		this.p5p18 = p5p18;
	}

	public String getP5p19() {
		return p5p19;
	}

	public void setP5p19(String p5p19) {
		this.p5p19 = p5p19;
	}

	public String getP5p20() {
		return p5p20;
	}

	public void setP5p20(String p5p20) {
		this.p5p20 = p5p20;
	}

	public String getP5p21() {
		return p5p21;
	}

	public void setP5p21(String p5p21) {
		this.p5p21 = p5p21;
	}

	public String getP5p22() {
		return p5p22;
	}

	public void setP5p22(String p5p22) {
		this.p5p22 = p5p22;
	}

	public String getP5p23() {
		return p5p23;
	}

	public void setP5p23(String p5p23) {
		this.p5p23 = p5p23;
	}

	public String getP5p24() {
		return p5p24;
	}

	public void setP5p24(String p5p24) {
		this.p5p24 = p5p24;
	}

	public Double getP6p1() {
		return p6p1;
	}

	public void setP6p1(Double p6p1) {
		this.p6p1 = p6p1;
	}

	public Double getP6p2() {
		return p6p2;
	}

	public void setP6p2(Double p6p2) {
		this.p6p2 = p6p2;
	}

	public Double getP6p3() {
		return p6p3;
	}

	public void setP6p3(Double p6p3) {
		this.p6p3 = p6p3;
	}

	public Double getP6p4() {
		return p6p4;
	}

	public void setP6p4(Double p6p4) {
		this.p6p4 = p6p4;
	}

	public Double getP6p5() {
		return p6p5;
	}

	public void setP6p5(Double p6p5) {
		this.p6p5 = p6p5;
	}

	public Double getP6p6() {
		return p6p6;
	}

	public void setP6p6(Double p6p6) {
		this.p6p6 = p6p6;
	}

	public String getP6p7() {
		return p6p7;
	}

	public void setP6p7(String p6p7) {
		this.p6p7 = p6p7;
	}

	public Double getP6p8() {
		return p6p8;
	}

	public void setP6p8(Double p6p8) {
		this.p6p8 = p6p8;
	}

	public Double getP6p9() {
		return p6p9;
	}

	public void setP6p9(Double p6p9) {
		this.p6p9 = p6p9;
	}

	public Double getP6p10() {
		return p6p10;
	}

	public void setP6p10(Double p6p10) {
		this.p6p10 = p6p10;
	}

	public Double getP6p11() {
		return p6p11;
	}

	public void setP6p11(Double p6p11) {
		this.p6p11 = p6p11;
	}

	public Double getP6p12() {
		return p6p12;
	}

	public void setP6p12(Double p6p12) {
		this.p6p12 = p6p12;
	}

	public Double getP6p13() {
		return p6p13;
	}

	public void setP6p13(Double p6p13) {
		this.p6p13 = p6p13;
	}

	public String getP6p14() {
		return p6p14;
	}

	public void setP6p14(String p6p14) {
		this.p6p14 = p6p14;
	}

	public Double getP6p15() {
		return p6p15;
	}

	public void setP6p15(Double p6p15) {
		this.p6p15 = p6p15;
	}

	public Double getP6p16() {
		return p6p16;
	}

	public void setP6p16(Double p6p16) {
		this.p6p16 = p6p16;
	}

	public Double getP6p17() {
		return p6p17;
	}

	public void setP6p17(Double p6p17) {
		this.p6p17 = p6p17;
	}

	public Double getP6p18() {
		return p6p18;
	}

	public void setP6p18(Double p6p18) {
		this.p6p18 = p6p18;
	}

	public Double getP6p19() {
		return p6p19;
	}

	public void setP6p19(Double p6p19) {
		this.p6p19 = p6p19;
	}

	public Double getP6p20() {
		return p6p20;
	}

	public void setP6p20(Double p6p20) {
		this.p6p20 = p6p20;
	}

	public String getP6p21() {
		return p6p21;
	}

	public void setP6p21(String p6p21) {
		this.p6p21 = p6p21;
	}

	public Double getP6p22() {
		return p6p22;
	}

	public void setP6p22(Double p6p22) {
		this.p6p22 = p6p22;
	}

	public Double getP6p23() {
		return p6p23;
	}

	public void setP6p23(Double p6p23) {
		this.p6p23 = p6p23;
	}

	public Double getP6p24() {
		return p6p24;
	}

	public void setP6p24(Double p6p24) {
		this.p6p24 = p6p24;
	}

	public Double getP6p25() {
		return p6p25;
	}

	public void setP6p25(Double p6p25) {
		this.p6p25 = p6p25;
	}

	public Double getP6p26() {
		return p6p26;
	}

	public void setP6p26(Double p6p26) {
		this.p6p26 = p6p26;
	}

	public Double getP6p27() {
		return p6p27;
	}

	public void setP6p27(Double p6p27) {
		this.p6p27 = p6p27;
	}

	public String getP6p28() {
		return p6p28;
	}

	public void setP6p28(String p6p28) {
		this.p6p28 = p6p28;
	}

	public Double getP6p29() {
		return p6p29;
	}

	public void setP6p29(Double p6p29) {
		this.p6p29 = p6p29;
	}

	public Double getP6p30() {
		return p6p30;
	}

	public void setP6p30(Double p6p30) {
		this.p6p30 = p6p30;
	}

	public Double getP6p31() {
		return p6p31;
	}

	public void setP6p31(Double p6p31) {
		this.p6p31 = p6p31;
	}

	public Double getP6p32() {
		return p6p32;
	}

	public void setP6p32(Double p6p32) {
		this.p6p32 = p6p32;
	}

	public Double getP6p33() {
		return p6p33;
	}

	public void setP6p33(Double p6p33) {
		this.p6p33 = p6p33;
	}

	public Double getP6p34() {
		return p6p34;
	}

	public void setP6p34(Double p6p34) {
		this.p6p34 = p6p34;
	}

	public Double getP6p35() {
		return p6p35;
	}

	public void setP6p35(Double p6p35) {
		this.p6p35 = p6p35;
	}

	public Double getP6p36() {
		return p6p36;
	}

	public void setP6p36(Double p6p36) {
		this.p6p36 = p6p36;
	}

	public Double getP6p37() {
		return p6p37;
	}

	public void setP6p37(Double p6p37) {
		this.p6p37 = p6p37;
	}

	public Double getP6p38() {
		return p6p38;
	}

	public void setP6p38(Double p6p38) {
		this.p6p38 = p6p38;
	}

	public Double getP6p39() {
		return p6p39;
	}

	public void setP6p39(Double p6p39) {
		this.p6p39 = p6p39;
	}

	public Double getP6p40() {
		return p6p40;
	}

	public void setP6p40(Double p6p40) {
		this.p6p40 = p6p40;
	}

	public Double getP6p41() {
		return p6p41;
	}

	public void setP6p41(Double p6p41) {
		this.p6p41 = p6p41;
	}

	public String getP6p42() {
		return p6p42;
	}

	public void setP6p42(String p6p42) {
		this.p6p42 = p6p42;
	}

	public Double getP6p43() {
		return p6p43;
	}

	public void setP6p43(Double p6p43) {
		this.p6p43 = p6p43;
	}

	public Double getP6p44() {
		return p6p44;
	}

	public void setP6p44(Double p6p44) {
		this.p6p44 = p6p44;
	}

	public Double getP6p45() {
		return p6p45;
	}

	public void setP6p45(Double p6p45) {
		this.p6p45 = p6p45;
	}

	public Double getP6p46() {
		return p6p46;
	}

	public void setP6p46(Double p6p46) {
		this.p6p46 = p6p46;
	}

	public Double getP6p47() {
		return p6p47;
	}

	public void setP6p47(Double p6p47) {
		this.p6p47 = p6p47;
	}

	public Double getP6p48() {
		return p6p48;
	}

	public void setP6p48(Double p6p48) {
		this.p6p48 = p6p48;
	}

	public String getP6p49() {
		return p6p49;
	}

	public void setP6p49(String p6p49) {
		this.p6p49 = p6p49;
	}

	public String getP7p8() {
		return p7p8;
	}

	public void setP7p8(String p7p8) {
		this.p7p8 = p7p8;
	}

	public String getP7p16() {
		return p7p16;
	}

	public void setP7p16(String p7p16) {
		this.p7p16 = p7p16;
	}

	public String getP7p24() {
		return p7p24;
	}

	public void setP7p24(String p7p24) {
		this.p7p24 = p7p24;
	}

	public String getP7p32() {
		return p7p32;
	}

	public void setP7p32(String p7p32) {
		this.p7p32 = p7p32;
	}

	public String getP7p40() {
		return p7p40;
	}

	public void setP7p40(String p7p40) {
		this.p7p40 = p7p40;
	}

	public String getP7p48() {
		return p7p48;
	}

	public void setP7p48(String p7p48) {
		this.p7p48 = p7p48;
	}

	public String getP7p56() {
		return p7p56;
	}

	public void setP7p56(String p7p56) {
		this.p7p56 = p7p56;
	}

	public String getP8p1() {
		return p8p1;
	}

	public void setP8p1(String p8p1) {
		this.p8p1 = p8p1;
	}

	public String getP8p2() {
		return p8p2;
	}

	public void setP8p2(String p8p2) {
		this.p8p2 = p8p2;
	}

	public String getP8p3() {
		return p8p3;
	}

	public void setP8p3(String p8p3) {
		this.p8p3 = p8p3;
	}

	public String getP8p4() {
		return p8p4;
	}

	public void setP8p4(String p8p4) {
		this.p8p4 = p8p4;
	}

	public String getP8p5() {
		return p8p5;
	}

	public void setP8p5(String p8p5) {
		this.p8p5 = p8p5;
	}

	public String getP8p6() {
		return p8p6;
	}

	public void setP8p6(String p8p6) {
		this.p8p6 = p8p6;
	}

	public String getP8p7() {
		return p8p7;
	}

	public void setP8p7(String p8p7) {
		this.p8p7 = p8p7;
	}

	public String getP8p8() {
		return p8p8;
	}

	public void setP8p8(String p8p8) {
		this.p8p8 = p8p8;
	}

	public String getP8p9() {
		return p8p9;
	}

	public void setP8p9(String p8p9) {
		this.p8p9 = p8p9;
	}

	public String getP8p10() {
		return p8p10;
	}

	public void setP8p10(String p8p10) {
		this.p8p10 = p8p10;
	}

	public String getP8p11() {
		return p8p11;
	}

	public void setP8p11(String p8p11) {
		this.p8p11 = p8p11;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getP7p64() {
		return p7p64;
	}

	public void setP7p64(String p7p64) {
		this.p7p64 = p7p64;
	}

	public String getP7p72() {
		return p7p72;
	}

	public void setP7p72(String p7p72) {
		this.p7p72 = p7p72;
	}

	public Double getP6p50() {
		return p6p50;
	}

	public void setP6p50(Double p6p50) {
		this.p6p50 = p6p50;
	}

	public Double getP6p51() {
		return p6p51;
	}

	public void setP6p51(Double p6p51) {
		this.p6p51 = p6p51;
	}

	public Double getP6p52() {
		return p6p52;
	}

	public void setP6p52(Double p6p52) {
		this.p6p52 = p6p52;
	}

	public Double getP6p53() {
		return p6p53;
	}

	public void setP6p53(Double p6p53) {
		this.p6p53 = p6p53;
	}

	public Double getP6p54() {
		return p6p54;
	}

	public void setP6p54(Double p6p54) {
		this.p6p54 = p6p54;
	}

	public Double getP6p55() {
		return p6p55;
	}

	public void setP6p55(Double p6p55) {
		this.p6p55 = p6p55;
	}

	public String getP6p56() {
		return p6p56;
	}

	public void setP6p56(String p6p56) {
		this.p6p56 = p6p56;
	}

	public Double getP6p57() {
		return p6p57;
	}

	public void setP6p57(Double p6p57) {
		this.p6p57 = p6p57;
	}

	public Double getP6p58() {
		return p6p58;
	}

	public void setP6p58(Double p6p58) {
		this.p6p58 = p6p58;
	}

	public Double getP6p59() {
		return p6p59;
	}

	public void setP6p59(Double p6p59) {
		this.p6p59 = p6p59;
	}

	public Double getP6p60() {
		return p6p60;
	}

	public void setP6p60(Double p6p60) {
		this.p6p60 = p6p60;
	}

	public Double getP6p61() {
		return p6p61;
	}

	public void setP6p61(Double p6p61) {
		this.p6p61 = p6p61;
	}

	public Double getP6p62() {
		return p6p62;
	}

	public void setP6p62(Double p6p62) {
		this.p6p62 = p6p62;
	}

	public String getP6p63() {
		return p6p63;
	}

	public void setP6p63(String p6p63) {
		this.p6p63 = p6p63;
	}

	public String getP2p6() {
		return p2p6;
	}

	public void setP2p6(String p2p6) {
		this.p2p6 = p2p6;
	}

	public String getP5p25() {
		return p5p25;
	}

	public void setP5p25(String p5p25) {
		this.p5p25 = p5p25;
	}

	public String getP6p64() {
		return p6p64;
	}

	public void setP6p64(String p6p64) {
		this.p6p64 = p6p64;
	}

	public String getP6p65() {
		return p6p65;
	}

	public void setP6p65(String p6p65) {
		this.p6p65 = p6p65;
	}

	public String getP6p66() {
		return p6p66;
	}

	public void setP6p66(String p6p66) {
		this.p6p66 = p6p66;
	}

	@Override
	public String toString() {
		return "ProjectReport [reportUuid=" + reportUuid + ", p1p1=" + p1p1 + ", p1p2=" + p1p2 + ", p1p3=" + p1p3
				+ ", p1p4=" + p1p4 + ", p1p5=" + p1p5 + ", p1p6=" + p1p6 + ", p1p78=" + p1p78 + ", p1p79=" + p1p79
				+ ", p1p80=" + p1p80 + ", p1p81=" + p1p81 + ", p1p82=" + p1p82 + ", p1p83=" + p1p83 + ", p1p84=" + p1p84
				+ ", p1p85=" + p1p85 + ", p1p86=" + p1p86 + ", p1p87=" + p1p87 + ", p1p88=" + p1p88 + ", p1p89=" + p1p89
				+ ", p1p90=" + p1p90 + ", p1p91=" + p1p91 + ", p1p92=" + p1p92 + ", p1p93=" + p1p93 + ", p1p94=" + p1p94
				+ ", p1p95=" + p1p95 + ", p2p1=" + p2p1 + ", p2p2=" + p2p2 + ", p2p3=" + p2p3 + ", p2p4=" + p2p4
				+ ", p2p5=" + p2p5 + ", p2p6=" + p2p6 + ", p4p1=" + p4p1 + ", p4p2=" + p4p2 + ", p4p3=" + p4p3
				+ ", p4p4=" + p4p4 + ", p4p5=" + p4p5 + ", p4p6=" + p4p6 + ", p4p7=" + p4p7 + ", p4p8=" + p4p8
				+ ", p4p9=" + p4p9 + ", p4p10=" + p4p10 + ", p4p11=" + p4p11 + ", p4p12=" + p4p12 + ", p4p13=" + p4p13
				+ ", p4p14=" + p4p14 + ", p4p15=" + p4p15 + ", p4p16=" + p4p16 + ", p4p17=" + p4p17 + ", p4p18=" + p4p18
				+ ", p5p1=" + p5p1 + ", p5p2=" + p5p2 + ", p5p3=" + p5p3 + ", p5p4=" + p5p4 + ", p5p5=" + p5p5
				+ ", p5p6=" + p5p6 + ", p5p7=" + p5p7 + ", p5p8=" + p5p8 + ", p5p9=" + p5p9 + ", p5p10=" + p5p10
				+ ", p5p11=" + p5p11 + ", p5p12=" + p5p12 + ", p5p13=" + p5p13 + ", p5p14=" + p5p14 + ", p5p15=" + p5p15
				+ ", p5p16=" + p5p16 + ", p5p17=" + p5p17 + ", p5p18=" + p5p18 + ", p5p19=" + p5p19 + ", p5p20=" + p5p20
				+ ", p5p21=" + p5p21 + ", p5p22=" + p5p22 + ", p5p23=" + p5p23 + ", p5p24=" + p5p24 + ", p5p25=" + p5p25
				+ ", p6p1=" + p6p1 + ", p6p2=" + p6p2 + ", p6p3=" + p6p3 + ", p6p4=" + p6p4 + ", p6p5=" + p6p5
				+ ", p6p6=" + p6p6 + ", p6p7=" + p6p7 + ", p6p8=" + p6p8 + ", p6p9=" + p6p9 + ", p6p10=" + p6p10
				+ ", p6p11=" + p6p11 + ", p6p12=" + p6p12 + ", p6p13=" + p6p13 + ", p6p14=" + p6p14 + ", p6p15=" + p6p15
				+ ", p6p16=" + p6p16 + ", p6p17=" + p6p17 + ", p6p18=" + p6p18 + ", p6p19=" + p6p19 + ", p6p20=" + p6p20
				+ ", p6p21=" + p6p21 + ", p6p22=" + p6p22 + ", p6p23=" + p6p23 + ", p6p24=" + p6p24 + ", p6p25=" + p6p25
				+ ", p6p26=" + p6p26 + ", p6p27=" + p6p27 + ", p6p28=" + p6p28 + ", p6p29=" + p6p29 + ", p6p30=" + p6p30
				+ ", p6p31=" + p6p31 + ", p6p32=" + p6p32 + ", p6p33=" + p6p33 + ", p6p34=" + p6p34 + ", p6p35=" + p6p35
				+ ", p6p36=" + p6p36 + ", p6p37=" + p6p37 + ", p6p38=" + p6p38 + ", p6p39=" + p6p39 + ", p6p40=" + p6p40
				+ ", p6p41=" + p6p41 + ", p6p42=" + p6p42 + ", p6p43=" + p6p43 + ", p6p44=" + p6p44 + ", p6p45=" + p6p45
				+ ", p6p46=" + p6p46 + ", p6p47=" + p6p47 + ", p6p48=" + p6p48 + ", p6p49=" + p6p49 + ", p6p50=" + p6p50
				+ ", p6p51=" + p6p51 + ", p6p52=" + p6p52 + ", p6p53=" + p6p53 + ", p6p54=" + p6p54 + ", p6p55=" + p6p55
				+ ", p6p56=" + p6p56 + ", p6p57=" + p6p57 + ", p6p58=" + p6p58 + ", p6p59=" + p6p59 + ", p6p60=" + p6p60
				+ ", p6p61=" + p6p61 + ", p6p62=" + p6p62 + ", p6p63=" + p6p63 + ", p6p64=" + p6p64 + ", p6p65=" + p6p65
				+ ", p6p66=" + p6p66 + ", p7p8=" + p7p8 + ", p7p16=" + p7p16 + ", p7p24=" + p7p24 + ", p7p32=" + p7p32
				+ ", p7p40=" + p7p40 + ", p7p48=" + p7p48 + ", p7p56=" + p7p56 + ", p7p64=" + p7p64 + ", p7p72=" + p7p72
				+ ", p8p1=" + p8p1 + ", p8p2=" + p8p2 + ", p8p3=" + p8p3 + ", p8p4=" + p8p4 + ", p8p5=" + p8p5
				+ ", p8p6=" + p8p6 + ", p8p7=" + p8p7 + ", p8p8=" + p8p8 + ", p8p9=" + p8p9 + ", p8p10=" + p8p10
				+ ", p8p11=" + p8p11 + ", project=" + project + "]";
	}

}
