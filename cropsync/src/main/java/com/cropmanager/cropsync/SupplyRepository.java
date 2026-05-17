    package com.cropmanager.cropsync;
    import java.util.List;
    import org.springframework.data.jpa.repository.JpaRepository;

    public interface SupplyRepository extends JpaRepository<Supply, Long> {
      List<Supply> findByUser_Username(String username);
    }