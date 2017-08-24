package net.nosegrind
//@Transactional
class ToolType implements Serializable{

	static hasMany = [archives:Arch]

	String toolName
	String protocol

	
	static constraints = {
	    toolName(nullable:false, unique:true)
		protocol (nullable: false, validator: {val, obj ->
			//boolean isValid = ['tcp','icmp','udp'].contains(val)
			if(!['tcp','icmp','udp'].contains(val)){ return ['INVALID TOOLTYPE PROTOCOL'] }
		})
	}

	static mapping = {
		cache true
	}

}

