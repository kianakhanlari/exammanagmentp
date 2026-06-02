package ir.maktab;


import ir.maktab.ui.Menue;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan("ir.maktab")
/*@PropertySource("classpath:application.properties")*/
public class Main {

    public static void main(String[] args) {

        ApplicationContext ctx =
                new AnnotationConfigApplicationContext(Main.class);

        Menue menue = ctx.getBean(Menue.class);
        menue.mainMenu();


      /*  Environment environment = ctx.getBean(Environment.class);

        System.out.println(
                environment.getProperty("spring.datasource.url")
        );*/
    }
}

