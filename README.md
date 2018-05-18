Pronghorn  [![Powered by CloudBees](https://www.cloudbees.com/sites/default/files/styles/large/public/Button-Powered-by-CB.png?itok=uMDWINfY)](https://pronghorn.ci.cloudbees.com/)
=====

Staged event driven single machine embedded micro-framework.

* **Garbage free message passing** design eliminates garbage collector stalls providing predictable data rates.  
* **Lock free non-blocking message passing** enables cores to make continuous progress at all times.  
* **Staged pipeline scheduler** enables optimization of the workload across cores. 
* Small memory footprint
* Smart thread scheduling

## Documentation
Please refer to the [wiki](../../wiki) for documentation, how to get started, and examples.

## Demo
Below is a recorded live demo of an application written using Pronghorn that quickly encodes and decodes JPG to raster (such as BMP) and vice versa. The project can be viewed [here](https://github.com/oci-pronghorn/JPG-Raster).

**Every Pronghorn project** receives an automatically generated, live telemetry page such as the one featured below.

![Decoding JPGs GIF](./static/DecodingJPGS.gif "Decoding JPGs")
![Encoding JPGs GIF](./static/EncodingJPGs.gif "Encoding JPGs")
	
## Usage

  To use this in your maven project add the following dependency.

    <dependency>
      <groupId>com.ociweb</groupId>
      <artifactId>Pronghorn</artifactId>
      <version>0.0.10-SNAPSHOT</version>
    </dependency> 
   
  Also add this public repository to your pom or settings.

    <repository>
      <releases>
        <enabled>false</enabled>
      </releases>
      <snapshots>
        <enabled>true</enabled>
      </snapshots>
      <id>repository-pronghorn.forge.cloudbees.com</id>
      <name>Active Repo for PronghornPipes</name>
      <url>http://repository-pronghorn.forge.cloudbees.com/snapshot/</url>
      <layout>default</layout>
    </repository>	

------------------------------------------

For more technical details please check out the Specification.md file.
Looking for the release jar? This project is under active development.

Please consider getting involved and sponsoring the completion of [Pronghorn](mailto:info@ociweb.com;?subject=Pronghorn%20Sponsor%20Inquiry)


Nathan Tippy, Principal Software Engineer [OCI](http://objectcomputing.com)  
Twitter: @NathanTippy
