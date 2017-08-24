package net.nosegrind

class Location implements Serializable{

	static mapWith = "mongo"

	String protocol
	String url
	String uri
	boolean hasMetadataKey=true
	
    static constraints = {
		protocol (nullable: false, validator: {val, obj ->
			if(!['http','https','ip'].contains(val)){ return ['INVALID LOCATION PROTOCOL']}
		})
		url(nullable:false)
		uri(nullable:true)
		hasMetadataKey(nullable:false)
    }

	static mapping = {
		cache true
	}
}
