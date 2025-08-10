

package com.wk.vpac.database.configuration;

import com.base.components.database.jpa.configuration.AbstractJpaConfiguration;
import com.base.components.database.jpa.dao.base.GenericJpaDaoImpl;
import com.wk.vpac.database.event.DictionaryLocalCacheBootInitEvent;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA Configuration
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version 1.0.0, 2017-11-21 11:30
 */
@Configuration
@EnableTransactionManagement
@EntityScan("com.wk.vpac.domain")
@EnableJpaRepositories(
  value = "com.wk.vpac.database.dao",
  repositoryBaseClass = GenericJpaDaoImpl.class
)
public class DatabaseConfiguration extends AbstractJpaConfiguration {

  @Bean
  public DictionaryLocalCacheBootInitEvent dictionaryLocalCacheBootInitEvent(){
    return new DictionaryLocalCacheBootInitEvent();
  }
}
