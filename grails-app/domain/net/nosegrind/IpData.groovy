package net.nosegrind
//@Transactional
class IpData implements Serializable{

	Location loc
	String ip
	String city
	String region
	String country
	String postal
	String latitude
	String longitude
	String timezone

	static constraints = {
		loc(nullable:true)
		ip(nullable:false)
		city(nullable:true)
		region(nullable:true)
		country(nullable:true)
		postal(nullable:true)
		latitude(nullable:false)
		longitude(nullable:false)
		timezone(nullable:true)
	}

	static mapping = {
		//cache true
	}

}

