package net.nosegrind.apiframework

import org.springframework.dao.DataIntegrityViolationException
import net.nosegrind.apitoolkit.*
import net.nosegrind.apiframework.HookService

class HookController {

	def springSecurityService
	HookService hookService

	static defaultAction = 'list'

	def list() {
		println("hooklist called...")
		def user = loggedInUser()
		def webhookList = (isSuperuser()) ? Hook.list() : Hook.findAllByUser(user, [max:params.max, sort:params.sort, order:params.order, offset:params.offset] )
		println(webhookList)
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		return ['hook':webhookList]
	}

	def create() {
		def formats = ['JSON','XML']
		Hook webhookInstance = Hook.findByUrlAndService(params.url,params.service)
		if(webhookInstance){
			render(status: 400,text:"URL EXISTS: PLEASE CHECK YOUR REGISTERED WEBHOOKS TO MAKE SURE THIS IS NOT A DUPLICATE.")
		}

		if(!hookService.validateUrl(params.url.toString())){
			render(status: 400,text:"BAD PROTOCOL: URL MUST BE FULLY QUALIFIED DOMAIN NAME (OR IP ADDRESS) FORMATTED WITH HTTP/HTTPS. PLEASE TRY AGAIN.")
		}

		webhookInstance = new Hook(params)
		webhookInstance.user=user

		if (webhookInstance.save(flush: true)) {
			return [hook:webhookInstance]
		}

		render(status: 400,text:"INVALID/MALFORMED DATA: PLEASE SEE DOCS FOR 'JSON' FORMED STRING AND PLEASE TRY AGAIN.")
	}

	def update() {
		def user = loggedInUser()

		Hook webhookInstance = Hook.findByIdAndPerson(params.id, user)
		if(!webhookInstance){
			render(status: 400,text:"WEBHOOK NOT FOUND: NO WEBHOOK WITH THAT ID FOUND BELONGING TO CURRENT USER.")
		}

		webhookInstance.url = params.url
		webhookInstance.format = params.format
		webhookInstance.service = params.service

		if (webhookInstance.save(flush: true)) {
			return [hook:webhookInstance]
		}

		render(status: 400,text:"INVALID/MALFORMED DATA: PLEASE SEE DOCS FOR 'JSON' FORMED STRING AND PLEASE TRY AGAIN.")
	}

	def show() {
		def user = loggedInUser()
		def webhookInstance = isSuperuser() ? Hook.get(params.id) : Hook.findByPersonAndId(user,params.id.toLong())
		return [hook:webhookInstance]
	}

	def delete() {
		def user = loggedInUser()
		def webhookInstance = isSuperuser() ? Hook.get(params.id) : Hook.findByPersonAndId(user,params.id.toLong())

		if (!webhookInstance) {
			render(status: 400,text:"WEBHOOK NOT FOUND: NO WEBHOOK WITH THAT ID FOUND BELONGING TO CURRENT USER.")
		}

		try {
			webhookInstance.delete(flush: true)
			return [hook:[id:params.id]]
		}catch (DataIntegrityViolationException e) {
			render(status: 500,text:"ISSUE DELETING WEBHOOK. Please try again or contact administrator")
		}
	}

	def reset() {
		def user = loggedInUser()

		Hook webhookInstance = Hook.findByIdAndUser(params.id, user)
		if(!webhookInstance){
			render(status: 400,text:"WEBHOOK NOT FOUND: NO WEBHOOK WITH THAT ID FOUND BELONGING TO CURRENT USER.")
		}

		webhookInstance.attempts = 0

		if (webhookInstance.save(flush: true)) {
			return [hook:[id:params.id]]
		}
	}

	protected loggedInUser() {
		return Person.load(springSecurityService.principal.id)
	}

	protected boolean isSuperuser() {
		return springSecurityService.principal.authorities*.authority.any { grailsApplication.config.apitoolkit.admin.roles.contains(it) }
	}
}
