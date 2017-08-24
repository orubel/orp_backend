package net.nosegrind
//@Transactional
class PacketRetransmit implements Serializable{

	PacketReport report
	Long timestamp
	String val

	static constraints = {
		report(nullable:false)
		timestamp(nullable:false)
		val(nullable:true)
	}

	static mapping = {
		//cache true
		report lazy:true
	}

}

