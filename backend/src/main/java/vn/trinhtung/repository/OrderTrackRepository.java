package vn.trinhtung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.trinhtung.entity.OrderTrack;

@Repository
public interface OrderTrackRepository extends JpaRepository<OrderTrack, Integer> {

}
