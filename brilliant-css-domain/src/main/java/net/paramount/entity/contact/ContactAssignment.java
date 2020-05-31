/*
* Copyright 2017, Bui Quy Duc
* by the @authors tag. See the LICENCE in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package net.paramount.entity.contact;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.paramount.domain.entity.general.Catalogue;
import net.paramount.entity.general.BusinessUnit;
import net.paramount.framework.entity.RepoAuditable;

/**
 * A contact.
 * 
 * @author Bui Quy Duc
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contact_assignment")
@EqualsAndHashCode(callSuper=false)
public class ContactAssignment extends RepoAuditable {
	private static final long serialVersionUID = 2823200391019340030L;

	@ManyToOne
	@JoinColumn(name = "owner_id")
	private ContactProfile owner;

	@ManyToOne
	@JoinColumn(name = "proxy_contact_id")
	private ContactCore proxy;

	@ManyToOne
	@JoinColumn(name = "direct_id")
	private BusinessUnit businessUnit;

	@ManyToOne
	@JoinColumn(name = "designation_catalogue_id")
	private Catalogue designation;

	@ManyToOne
	@JoinColumn(name = "job_info_catalogue_id")
	private Catalogue jobInfo;
	
	@Column(name="issued_date")
  private Date issuedDate;

  @Column(name="expired_date")
  private Date expiredDate;

}
