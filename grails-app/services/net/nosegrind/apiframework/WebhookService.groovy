package net.nosegrind.apiframework

import grails.converters.JSON
import grails.converters.XML
import org.grails.validation.routines.UrlValidator

class WebhookService {

    def grailsApplication

    static transactional = false

    void postData(String service, Map data, String state) {
        send(data, state, service)
    }

    void postData(String service, Object data, String state) {
        data = formatDomainObject(data)
        send(data, state, service)
    }

    private boolean send(Map data, String state, String service) {

        def hooks = grailsApplication.getClassForName(grailsApplication.config.webhook.domain).findAll("from Webhook where service='${service}'")

        hooks.each { hook ->
            String format = hook.format.toLowerCase()
            if(hook.attempts>=grailsApplication.config.webhook.attempts){
                data = 	[message:'Number of attempts exceeded. Please reset webhook via web interface']
            }
            String hookData

            try{
                def conn = hook.url.toURL().openConnection()
                conn.setRequestMethod("POST")
                conn.doOutput = true
                def queryString = []
                switch(format){
                    case 'xml':
                        hookData = (data as XML).toString()
                        queryString << "state=${state}&xml=${hookData}"
                        break
                    case 'json':
                    default:
                        hookData = (data as JSON).toString()
                        queryString << "state=${state}&json=${hookData}"
                        break
                }
                def writer = new OutputStreamWriter(conn.outputStream)
                writer.write(queryString)
                writer.flush()
                writer.close()
                conn.connect()
                if(conn.content.text!='connected'){
                    hook.attempts+=1
                    hook.save(flush: true)
                    log.info("[Webhook] main.scripts.net.nosegrind.apiframework.WebhookService : No Url ${hook.url} found")
                }
            }catch(Exception e){
                hook.attempts+=1
                hook.save(flush: true)
                log.info("[Webhook] main.scripts.net.nosegrind.apiframework.WebhookService : " + e)
            }
        }

    }

    Map formatDomainObject(Object data){
        def nonPersistent = ["log", "class", "constraints", "properties", "errors", "mapping", "metaClass","maps"]
        def newMap = [:]
        data.getProperties().each { key, val ->
            if (!nonPersistent.contains(key)) {
                if(grailsApplication.isDomainClass(val.getClass())){
                    newMap.put key, val.id
                }else{
                    newMap.put key, val
                }
            }
        }
        return newMap
    }

    Map processMap(Map data,Map processor){
        processor.each() { key, val ->
            if(!val?.trim()){
                data.remove(key)
            }else{
                def matcher = "${data[key]}" =~ "${data[key]}"
                data[key] = matcher.replaceAll(val)
            }
        }
        return data
    }

    boolean validateUrl(String url){
        String[] schemes = ["http","https"]
        UrlValidator urlValidator = new UrlValidator(schemes)
        if (urlValidator.isValid(url)) {
            return true
        }else{
            return false
        }
    }
}
