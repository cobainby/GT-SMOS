package com.southgt.smosplat.organ;

import java.util.Date;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.southgt.smosplat.data.util.math.GtMath;
import com.southgt.smosplat.organ.entity.Device;
import com.southgt.smosplat.project.entity.Mcu;
import com.southgt.smosplat.project.entity.MonitorItem;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.Section;
import com.southgt.smosplat.project.entity.Stress;
import com.southgt.smosplat.project.entity.SurveyPoint;
import com.southgt.smosplat.project.entity.SurveyPoint_CX;
import com.southgt.smosplat.project.entity.SurveyPoint_LZ;
import com.southgt.smosplat.project.entity.SurveyPoint_SW;
import com.southgt.smosplat.project.entity.SurveyPoint_WYD;
import com.southgt.smosplat.project.entity.SurveyPoint_WYS;
import com.southgt.smosplat.project.entity.SurveyPoint_ZC;
import com.southgt.smosplat.project.entity.Warning;
import com.southgt.smosplat.project.service.ISurveyPoint_WYSService;


public class TestXmlFunc {
	@Resource
	ISurveyPoint_WYSService sp_WYSService;
	@Test
	public void testFunc(){
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
		SessionFactory sf=(SessionFactory) ac.getBean("sessionFactory");
		Session session=sf.openSession();
		Transaction tx=session.beginTransaction();
		
		SurveyPoint_WYS d = (SurveyPoint_WYS) session.get(SurveyPoint_WYS.class,"dd33058f-a19c-43f9-a901-03425b47efb9");
		d.setProcessedDataUuid("ef32-ddf");
		sp_WYSService.deleteEntity(d);
//		session.update(d);
//		tx.commit();
//		session.close();
//		sf.close();
//		((ClassPathXmlApplicationContext)ac).close();
//		String angle = "161° 53' 46.20485\"";
//		int degIndex = angle.indexOf("°");
//		int minIndex = angle.indexOf("'");
//		int secIndex = angle.indexOf("\"");
//		double deg = Double.parseDouble(angle.substring(0, degIndex));
//		double min = Double.parseDouble(angle.substring(degIndex + 1, minIndex));
//		double sec = Double.parseDouble(angle.substring(minIndex + 1, secIndex));
//		double a = deg + min/100 + sec/10000;
//		double a2Radian = GtMath.angleToRadian(a,1);
//		System.out.println();
	}
}
