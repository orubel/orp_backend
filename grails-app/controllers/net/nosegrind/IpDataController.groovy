package net.nosegrind



//import grails.transaction.Transactional
//import main.scripts.net.nosegrind.apiframework.TraceService

//@Transactional
class IpDataController {

	//TraceService traceService

	def getById(){
		def result = IpData.executeQuery("select new map(IP.ip as ip, IP.city as city, IP.region as region, IP.country as country, IP.timezone as timezone) from IpData as IP where IP.id=?",[params.id.toLong()])
		return [ipData:result]
	}


}
