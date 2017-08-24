package net.nosegrind.apiframework

class Hook {

	Person user
	String url
	String format = 'JSON'
	String service
	Long attempts = 0
	Boolean isEnabled = true
	Date dateCreated
	Date lastModified = new Date()

	static mapping = {
		//datasource 'user'
	}
	
	static constraints = {
		user(nullable:false)
		url(nullable:false)
		format(nullable:false)
		service(nullable:false)
		attempts(nullable:false)
	}
}
