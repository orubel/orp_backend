package net.nosegrind.apiframework

import grails.transaction.Transactional


class Person implements Serializable{


	static transients = ['hasBeforeInsert','hasBeforeValidate','hasBeforeUpdate','springSecurityService']

	transient hasBeforeInsert = false
	transient hasBeforeValidate = false
	transient hasBeforeUpdate = false
	transient springSecurityService

	String username
	String password
	String email
	String oauthId
	String oauthProvider
	String avatarUrl
	boolean enabled=true
	boolean accountExpired=false
	boolean accountLocked=false
	boolean passwordExpired=false

/*
	Person(String username, String password) {
		this()
		this.username = username
		this.password = password
	}
	*/


	@Override
	int hashCode() {
		username?.hashCode() ?: 0
	}

	@Override
	boolean equals(other) {
		is(other) || (other instanceof Person && other.username == username)
	}

	@Override
	String toString() {
		username
	}

	Set<Role> getAuthorities() {
		PersonRole.findAllByPerson(this)*.role
	}

	def beforeInsert() {
		if (!hasBeforeInsert) {
			hasBeforeInsert = true
			encodePassword()
		}
	}

	def afterInsert() {
		hasBeforeInsert = false
	}

	def beforeUpdate() {
		if (!hasBeforeUpdate) {
			if (isDirty('password')) {
				hasBeforeUpdate = true
				encodePassword()
			}
		}
	}

	def afterUpdate() {
		hasBeforeUpdate = false
	}

	protected void encodePassword() {
		password = springSecurityService.encodePassword(password)
	}

	/*
	protected void encodePassword() {
		password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
	}
	*/

	static constraints = {
		username blank: false, unique: true
		password blank: false
		email(nullable:false,email:true, unique: true,maxSize:100)
		oauthId(nullable: true)
		oauthProvider(nullable: true)
		avatarUrl(nullable: true)
	}

	static mapping = {
		//datasource 'user'
		password column: '`password`'
		cache true
	}
}
