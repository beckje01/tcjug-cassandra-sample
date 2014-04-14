package sample

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session
import com.datastax.driver.core.policies.ConstantReconnectionPolicy
import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy
import com.datastax.driver.core.policies.DowngradingConsistencyRetryPolicy
import com.datastax.driver.core.policies.RoundRobinPolicy;
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {

	@Bean
	public static Session session() {
		Cluster.Builder builder = Cluster.builder()
		builder.addContactPoint("127.0.0.1")
			.withRetryPolicy(DowngradingConsistencyRetryPolicy.INSTANCE)
			.withReconnectionPolicy(new ConstantReconnectionPolicy(100L))
            .withLoadBalancingPolicy(new RoundRobinPolicy())

		return builder.build().connect("simple")
	}

	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = SpringApplication.run(Application.class, args)

		System.out.println("Application Started")

	}
}
