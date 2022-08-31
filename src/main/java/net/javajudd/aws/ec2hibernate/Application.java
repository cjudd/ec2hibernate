package net.javajudd.aws.ec2hibernate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.BlockDeviceMapping;
import software.amazon.awssdk.services.ec2.model.CreateTagsRequest;
import software.amazon.awssdk.services.ec2.model.EbsBlockDevice;
import software.amazon.awssdk.services.ec2.model.HibernationOptionsRequest;
import software.amazon.awssdk.services.ec2.model.InstanceType;
import software.amazon.awssdk.services.ec2.model.RunInstancesRequest;
import software.amazon.awssdk.services.ec2.model.RunInstancesResponse;

@SpringBootApplication
public class Application implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Ec2Client ec2 = Ec2Client.builder()
				.region(Region.US_EAST_2)
				.build();

		RunInstancesRequest runRequest = RunInstancesRequest.builder()
				.imageId("ami-081f2c86d8b025c4b")
				.instanceType(InstanceType.T3_MEDIUM)
				.maxCount(1)
				.minCount(1)
				.keyName("dev-key")
				.blockDeviceMappings(BlockDeviceMapping.builder()
						.deviceName("/dev/xvda")
						.ebs(EbsBlockDevice.builder()
								.volumeSize(40)
								.deleteOnTermination(true)
								.encrypted(true)
								.build())
						.build())
				.hibernationOptions(HibernationOptionsRequest.builder().configured(true).build())
				.securityGroups("devvm-default-sg")
				.build();

		RunInstancesResponse response = ec2.runInstances(runRequest);
		String instanceId = response.instances().get(0).instanceId();

		software.amazon.awssdk.services.ec2.model.Tag nameTag = software.amazon.awssdk.services.ec2.model.Tag.builder()
				.key("Name").value("hibernate example")
				.build();

		CreateTagsRequest tagRequest = CreateTagsRequest.builder()
				.resources(instanceId)
				.tags(nameTag)
				.build();

		ec2.createTags(tagRequest);
	}
}
