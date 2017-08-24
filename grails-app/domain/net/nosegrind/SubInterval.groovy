package net.nosegrind
//@Transactional
class SubInterval implements Serializable{


	String start
	String duration
	String val

	static constraints = {
		start(nullable:false)
		duration(nullable:false)
		val(nullable:false)
	}

	static mapping = {
		//cache true
	}

}

