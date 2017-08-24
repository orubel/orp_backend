package net.nosegrind
//@Transactional
class PacketTrace implements Serializable{

	PacketReport report
	Long timestamp
	Location ip
	String rtt
	Integer ttl


	static constraints = {
		report(nullable:false)
		timestamp(nullable:false)
		ip(nullable:false)
		rtt(nullable:false)
		ttl(nullable:false)
	}

	static mapping = {
		//cache true
		report lazy:true
		ip lazy:true
	}

}

