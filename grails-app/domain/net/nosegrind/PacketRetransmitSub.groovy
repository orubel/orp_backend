package net.nosegrind
//@Transactional
class PacketRetransmitSub implements Serializable{

	PacketRetransmit packetRetransmit
	SubInterval sub

	static constraints = {
		packetRetransmit(nullable:false)
		sub(nullable:false)
	}

	static mapping = {
		//cache true
		packetRetransmit lazy:true
		sub lazy:true
	}

}

