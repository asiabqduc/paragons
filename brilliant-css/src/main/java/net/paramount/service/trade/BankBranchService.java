package net.paramount.service.trade;

import java.util.Optional;

import net.paramount.entity.trade.BankBranch;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.service.GenericService;

public interface BankBranchService extends GenericService<BankBranch, Long> {

	/**
	 * Get one Bank with the provided name.
	 * 
	 * @param name
	 *            The Bank Branch name
	 * @return The Bank Branch
	 * @throws ObjectNotFoundException
	 *             If no such Bank Branch exists.
	 */
	Optional<BankBranch> getByName(String name) throws ObjectNotFoundException;

	/**
	 * Get one Bank with the provided code.
	 * 
	 * @param code
	 *            The Bank Branch code
	 * @return The Bank Branch
	 * @throws ObjectNotFoundException
	 *             If no such Bank Branch exists.
	 */
	Optional<BankBranch> getByCode(String code) throws ObjectNotFoundException;
}
