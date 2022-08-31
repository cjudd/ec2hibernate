# AWS EC2 Hibernate Java SDK v2 Example

This provides an example of how to create an EC2 Instance in AWS that has the capability of hibernating. This can be useful when automating the creation of developer VMs.

There are 2 requirements when making an EC2 instance hibernatable and these requirements must be fulfilled when the instance is created. The EBS drive must be encrypted and the hibernate option must be set.