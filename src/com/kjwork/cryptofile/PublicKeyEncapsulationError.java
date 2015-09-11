package com.kjwork.cryptofile;

public class PublicKeyEncapsulationError extends Throwable {

	public PublicKeyEncapsulationError() {
	}

	public PublicKeyEncapsulationError(String msg) {
		this.message = msg;
	}

	public PublicKeyEncapsulationError(Throwable t) {
		this.throwable = t;
	}
	
	public String toString() {
		return this.message;
	}
	
	public Throwable throwable;
	public String message;
}
