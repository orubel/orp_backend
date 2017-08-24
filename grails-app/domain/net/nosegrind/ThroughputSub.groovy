package net.nosegrind
//@Transactional
class ThroughputSub implements Serializable{

	Throughput throughput
	SubInterval sub

	static constraints = {
		throughput(nullable:false)
		sub(nullable:false)
	}

	static mapping = {
		//cache true
		throughput lazy:true
		sub lazy:true
	}

}

