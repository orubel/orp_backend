package net.nosegrind
//@Transactional
class OwDelay implements Serializable{

	PacketReport report
	Long timestamp
	String maximum
	String mean
	String median
	String minimum
	String mode
	String standardDeviation
	String variance
	String vals


	static constraints = {
		report(nullable:false)
		timestamp(nullable:false)
		maximum(nullable:true)
		mean(nullable:true)
		median(nullable:true)
		minimum(nullable:true)
		mode(nullable:true)
		standardDeviation(nullable:true)
		variance(nullable:true)
		vals(size: 1..50000, nullable:false)
	}

	static mapping = {
		//cache true
		vals type: 'text'
		report lazy:true
	}

}

