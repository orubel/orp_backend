package net.nosegrind



//import grails.transaction.Transactional
//import main.scripts.net.nosegrind.apiframework.TraceService

//@Transactional
class PacketReportController {

	//TraceService traceService

	def getPacketReport(){
		def packetReport =  PacketReport.get(params.id.toLong())
		return [packetReport:packetReport]
	}

	def listByEventType(){
		EventType eType = EventType.get(params.id.toLong())
		def packetReport =  PacketReport.withCriteria() {
			order('xAxis','asc')
			eq("eventType", eType)
		}

		return [packetReport:packetReport]
	}

	def getArchiveLocationsByEvent(){
		//def max = (params.max)?params.max:0
		//def offset = (params.offset)?params.offset:0
		def result = PacketReport.executeQuery("select new map(PR.id as id,LIS.url as inputSource, LID.url as inputDestination) from PacketReport as PR join PR.arch as A join A.inputSource as LIS join A.inputDestination as LID where PR.eventType.id = ?",[params.id.toLong()],[max: params.max, offset: params.offset])
		return [packetReport:result]
	}


	def getArchiveLocationsByEventAndPacketLossRateDateRange(){
		Long endDate = System.currentTimeMillis() / 1000L
		Long diff = ((60*60)*24)*28
		Long startDate = (endDate-diff).toLong()
		println("endDate :"+endDate)
		println("startDate :"+startDate)
		println("select PLR.id from PacketLossRate as PLR where PLR.xAxis<=${endDate} and PLR.xAxis>=${startDate} group by PLR.id")
		def result = PacketReport.executeQuery("select new map(PR.id as id,LIS.url as inputSource, LID.url as inputDestination) from PacketReport as PR join PR.arch as A join A.inputSource as LIS join A.inputDestination as LID where PR.eventType.id = ? and PR.id in (select PLR.id from PacketLossRate as PLR where PLR.xAxis<=${endDate} and PLR.xAxis>=${startDate} group by PLR.id)",[params.id.toLong()],[max: params.max, offset: params.offset])
		println(result)
		return [packetReport:result]
	}
}
