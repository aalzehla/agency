//import com._3line.gravity.GravityApplication;
//import com._3line.gravity.core.sms.service.SmsService;
//import com._3line.gravity.freedom.agents.repository.AgentsRepository;
//import org.apache.velocity.Template;
//import org.apache.velocity.VelocityContext;
//import org.apache.velocity.app.Velocity;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.io.StringWriter;
//
//import static org.junit.Assert.assertNotNull;
//
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = GravityApplication.class)
//public class MainTest {
//
//
////    @Autowired
////    private AgentsRepository agentsRepository;
////
////    @Qualifier("IPIntgratedSMSImplementation")
////    @Autowired
////    SmsService smsService;
//
//    @Autowired
//    BCryptPasswordEncoder passwordEncoder;
//
////    @Test
////    public void checkAgent() {
////
////        System.out.println("am searching");
////        Agents foundEntity = agentsRepository
////                .findByUsername("funmilayo.olakunle");
////
////        System.out.println("here :: "+foundEntity.getWalletNumber());
////        assertNotNull(foundEntity);
////    }
//
//
//    @Test
//    public void testSMS(){
//        String res = passwordEncoder.encode("1111");
//        System.out.println("encrypted pass is :: "+res);
//        System.out.println("here ::"+passwordEncoder.matches("1111",res));
//    }
//
//    @Test
//    public void testvm(){
////        Template template;
////        StringWriter writer = new StringWriter();
////        VelocityContext context = new VelocityContext();
////        context.put("name","owolabi");
////        template = Velocity.getTemplate("agent_creation");
////        template.merge(context,writer);
////        System.out.println("output :: "+writer);
////        String vmstr = "Congratulations !! , welcome to Freedom network, find your credentials below, ensure to onboard your pos terminal !name : %s agentid : %s Pin : %s Password : %s";
////        String finalMessage = String.format(vmstr,"user.name","owiwo","1111","pass@123");
////        System.out.println("Final message :: "+finalMessage);
//
//        String term = "2035";
////        Long myval = 57274943842;
//        System.out.println(term.substring(1,4));
//    }
//
//}
