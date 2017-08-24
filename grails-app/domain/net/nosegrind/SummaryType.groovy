package net.nosegrind
//@Transactional
class SummaryType implements Serializable{

	static hasMany = [reports:PacketReport]

	String name

	
	static constraints = {
	    name(nullable:false, unique:true)
	}

	static mapping = {
		cache true
	}

}

