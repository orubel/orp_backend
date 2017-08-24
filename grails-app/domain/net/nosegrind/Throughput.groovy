package net.nosegrind
//@Transactional
class Throughput implements Serializable{

	PacketReport report
	Long timestamp
	
	static constraints = {
		report(nullable:false)
		timestamp(nullable:false)
	}

	static mapping = {
		//cache true
		report lazy:true
	}

}

