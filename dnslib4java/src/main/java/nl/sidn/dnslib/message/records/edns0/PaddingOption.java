/*
 * ENTRADA, a big data platform for network data analytics
 *
 * Copyright (C) 2016 SIDN [https://www.sidn.nl]
 * 
 * This file is part of ENTRADA.
 * 
 * ENTRADA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ENTRADA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ENTRADA.  If not, see [<http://www.gnu.org/licenses/].
 *
 */	
package nl.sidn.dnslib.message.records.edns0;

import nl.sidn.dnslib.message.util.NetworkData;

/**
 * @see https://tools.ietf.org/html/rfc7830
 * 
 *
 */
public class PaddingOption extends EDNS0Option{
	
	private int length;
	
	public PaddingOption(){}

	public PaddingOption(int code, int len,NetworkData buffer) {
		super(code, len, buffer);
		this.length = len;
	}

	public int getLength() {
		return length;
	}

	@Override
	public String toString() {
		return "PaddingOption [length=" + length + "]";
	}
	

}
