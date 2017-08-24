package net.nosegrind
//@Transactional
class PacketLossRate implements Serializable{

	PacketReport report
	// Unix Timestamp
	Long xAxis
	// Value
	String yAxis


	static constraints = {
		report(nullable:false)
		xAxis(nullable:false)
		yAxis(nullable:false)
	}

	static mapping = {
		//cache true
		report lazy:true
	}

}

