/*
 * Copyright 2013 Ambud Sharma
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.2
 */
package org.ambud.marauder.event.taxonomy;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.ambud.marauder.event.MarauderEventTypes;

@Entity
@Table(name="mev_attr")
@NamedQuery(name="MarauderEventAttribute.findAll",query="select e from MarauderEventAttribute e")
public class MarauderEventAttribute implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Column(nullable=false)
	@Id
	private String uuid;
	@Column(name="attr", nullable=false)
	private String attributeName;
	@Column(name="attr_al", nullable=false)
	private String attributeAlias;
	@Column(name="tp", nullable=false)
	@Enumerated(EnumType.STRING)
	private MarauderEventTypes type;
	@Column(name="idx", nullable=false)
	private boolean isIndexed;
	@Column(name="val_tp", nullable=false)
	@Enumerated(EnumType.ORDINAL)
	private MarauderAttributeValueType valueType;
	
	public MarauderEventAttribute() {
	}

	public MarauderEventAttribute(String uuid, String attributeName,
			String attributeAlias, MarauderEventTypes type, boolean isIndexed, MarauderAttributeValueType valueType) {
		this.uuid = uuid;
		this.attributeName = attributeName;
		this.attributeAlias = attributeAlias;
		this.type = type;
		this.isIndexed = isIndexed;
		this.valueType = valueType;
	}

	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the attributeName
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * @param attributeName the attributeName to set
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	/**
	 * @return the attributeAlias
	 */
	public String getAttributeAlias() {
		return attributeAlias;
	}

	/**
	 * @param attributeAlias the attributeAlias to set
	 */
	public void setAttributeAlias(String attributeAlias) {
		this.attributeAlias = attributeAlias;
	}

	/**
	 * @return the type
	 */
	public MarauderEventTypes getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(MarauderEventTypes type) {
		this.type = type;
	}

	/**
	 * @return the isIndexed
	 */
	public boolean isIndexed() {
		return isIndexed;
	}

	/**
	 * @param isIndexed the isIndexed to set
	 */
	public void setIndexed(boolean isIndexed) {
		this.isIndexed = isIndexed;
	}

	/**
	 * @return the valueType
	 */
	public MarauderAttributeValueType getValueType() {
		return valueType;
	}

	/**
	 * @param valueType the valueType to set
	 */
	public void setValueType(MarauderAttributeValueType valueType) {
		this.valueType = valueType;
	}

	@Override
	public String toString() {
		return "Attribute:"+attributeAlias+" ValueType:"+getValueType();
	}
}
