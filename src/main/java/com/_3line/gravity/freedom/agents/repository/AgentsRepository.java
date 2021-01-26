package com._3line.gravity.freedom.agents.repository;

import com._3line.gravity.freedom.agents.models.Agents;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface AgentsRepository extends JpaRepository<Agents, Long> {

    Agents findByAccountNo(String accountNo);

    Agents findFirstByAccountNo(String accountNo);

    Agents findByUsername(String username);

    Page<Agents> findByUsernameContainsOrTerminalIdContains(String username,String terminalId,Pageable pageable);


    Page<Agents> findByActivatedAndSanefAgentCodeIsNull(Integer isActive,Pageable pageable);


    Agents findByUsernameOrAgentIdOrWalletNumberOrIncomeWalletNumber(String username,String agentId,String walletNumber, String incomeWalletNumber);

    Agents findByWalletNumber(String walletNumber);

    Agents findByAgentId(String agentId);

    Agents findByAgentIdOrUsername(String agentId,String username);

    Agents findByTerminalId(String terminalId);

//    Agents findByTerminalIdAndTerminalId(String terminalId);

    List<Agents> findByEmail(String email);



    @Query(value="select a.agent_id,a.username,a.terminal_id,\n" +
            "a.wallet_number,a.phone_number,a.activated,\n" +
            "a.id from agents a ",nativeQuery = true)
    List<Object[]> fetchForAgentView();


    @Query(value = "select * from agents",nativeQuery = true)
    List<Agents> fetchAllAgents();

    Agents findByPhoneNumber(String phoneNumber);

    List<Agents> findByAgentTypeContains(String agentType);

    List<Agents>findByParentAgentIdOrderByDatecreatedDesc(Long agentId);

    List<Agents>findByParentAgentIdAndAgentTypeOrderByDatecreatedDesc(Long agentId,String agentType);

    List<Agents>findBySubParentAgentIdOrderByDatecreatedDesc(Long agentId);

    List<Agents> findByParentAgentIdOrderByDatecreatedDesc(Long agentId, Pageable pageable);

    Agents findByAgentIdAndParentAgentId(String agentId,Long parentAgentId);

    List<Agents>findByDatecreatedBetween(Date startDate, Date endDate);
}
