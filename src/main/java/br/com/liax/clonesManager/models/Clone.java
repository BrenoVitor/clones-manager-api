package br.com.liax.clonesManager.models;

import java.util.Collection;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Clone {
	
	private long id;
	private String name;
	private int age;
	private Date createdAt;
	private Collection<Additional> additionals;
	
	public Clone() {
	}
	
	public Collection<Additional> getAdditionals() {
		return additionals;
	}

	public void setAdditionals(Collection<Additional> additionals) {
		this.additionals = additionals;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Clone [id=" + id + ", name=" + name + ", age=" + age + ", createdAt=" + createdAt + ", additionals="
				+ additionals + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((additionals == null) ? 0 : additionals.hashCode());
		result = prime * result + age;
		result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Clone other = (Clone) obj;
		if (additionals == null) {
			if (other.additionals != null)
				return false;
		} else if (!additionals.equals(other.additionals))
			return false;
		if (age != other.age)
			return false;
		if (createdAt == null) {
			if (other.createdAt != null)
				return false;
		} else if (!createdAt.equals(other.createdAt))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	

}
