/*
 * Copyleft 2007-2011 Ozgur Yazilim A.S.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * www.tekir.com.tr
 * www.ozguryazilim.com.tr
 *
 */

package net.paramount.entity.trade;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.paramount.entity.contact.CTAContact;
import net.paramount.entity.doc.DocumentType;
import net.paramount.entity.general.WorkBunch;
import net.paramount.framework.entity.RepoEntity;
import net.paramount.model.ChequeStatus;

/**
 * Cheque Detail
 * 
 * @author dumlupinar
 */
@Entity
@Table(name="PROMISSORY_NOTE_HISTORY")
public class PromissoryNoteHistory extends RepoEntity {

    private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name="OWNER_ID")
	private PromissoryNote owner;
	
	@Column(name="PROCESS_DATE")
    @Temporal(TemporalType.DATE)
    private Date date = new Date();
	
	@Column(name="STATUS")
	@Enumerated(EnumType.ORDINAL)
	private ChequeStatus status;

	@Column(name="INFO")
	private String info;
	
	/**
	 * Kaydın geldiği kaynak
	 */
	@Column(name="SOURCE")
	@Enumerated(EnumType.ORDINAL)
	private DocumentType source;
	
	/**
	 * Kaydın geldiği kaynağın id si
	 */
    @Column(name="SOURCE_ID")
    private Long sourceId;
    
    @Column(name="SERIAL", length=10)
    private String serial;

    /* Hangi banka hesabinda islem goruyor */
    @ManyToOne
    @JoinColumn(name="BANK_ACCOUNT_ID")
    private BankAccount bankAccount;

    @ManyToOne
    @JoinColumn(name="CONTACT_ID")
    private CTAContact contact;

    @ManyToOne
    @JoinColumn(name="ACCOUNT_ID")
    private Account account;

    @ManyToOne
    @JoinColumn(name="WORK_BUNCH_ID")
    private WorkBunch workBunch;

	public PromissoryNote getOwner() {
		return owner;
	}

	public void setOwner(PromissoryNote owner) {
		this.owner = owner;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public ChequeStatus getStatus() {
		return status;
	}

	public void setStatus(ChequeStatus status) {
		this.status = status;
	}

    public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public DocumentType getSource() {
		return source;
	}

	public void setSource(DocumentType source) {
		this.source = source;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

    @Override
    public String toString() {
        return "PaymentItemChequeDetail[id=" + getId() + "]";
    }

	public WorkBunch getWorkBunch() {
		return workBunch;
	}

	public void setWorkBunch(WorkBunch workBunch) {
		this.workBunch = workBunch;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	public CTAContact getContact() {
		return contact;
	}

	public void setContact(CTAContact contact) {
		this.contact = contact;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
	

}
