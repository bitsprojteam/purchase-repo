/**
 * 
 */
package edu.bitspilani.mtech.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.bitspilani.mtech.model.Purchase;

/**
 * @author Mohamed Noohu
 *
 */
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

}
