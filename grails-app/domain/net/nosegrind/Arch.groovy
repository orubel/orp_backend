package net.nosegrind
//@Transactional
class Arch implements Serializable{

	static hasMany = [reports:PacketReport]

	// base url/uri for all child events
	Location measurementLocation
	String metadataKey
	ToolType toolType
	// AKA destination
	Location returnSource
	// AKA source
	Location returnDestination
	Location measurementAgent
	Location inputSource
	Location inputDestination
	Integer traceMaxTtl
	Integer timeInterval
	Integer ipPacketSize


	static constraints = {
		measurementLocation(nullable:false)
		metadataKey(nullable:false)
		toolType(nullable:false)
		returnSource(nullable:false)
		returnDestination(nullable:false)
		measurementAgent(nullable:false)
		inputSource(nullable:false)
		inputDestination(nullable:false)
		traceMaxTtl(nullable:true)
		timeInterval(nullable:false)
		ipPacketSize(nullable:true)
	}

	static mapping = {
		//cache true
		measurementLocation lazy:true
		toolType lazy: true
		returnSource lazy: true
		returnDestination lazy: true
		measurementAgent lazy: true
		inputSource lazy: true
		inputDestination lazy: true
	}

}

