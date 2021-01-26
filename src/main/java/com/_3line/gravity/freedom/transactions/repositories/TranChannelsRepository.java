/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.transactions.repositories;

import com._3line.gravity.freedom.transactions.models.TranChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author NiyiO
 */

@Repository
public interface TranChannelsRepository extends JpaRepository<TranChannel, Long> {

    TranChannel findByChannelName(String channelName);

}
