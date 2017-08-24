package net.nosegrind
//@Transactional
class PacketReport implements Serializable{

	Arch arch
	String baseUri
	String summaryUri
	SummaryType summaryType
	EventType eventType
	Long timestamp

	// time = xaxis
	static constraints = {
		arch(nullable:false)
		baseUri(nullable:false)
		summaryType(nullable:true)
		summaryUri(nullable:true)
		eventType(nullable:false)
		timestamp(nullable:true)
	}

	static mapping = {
		//cache true
		arch lazy:true
		eventType lazy:true
	}

}

