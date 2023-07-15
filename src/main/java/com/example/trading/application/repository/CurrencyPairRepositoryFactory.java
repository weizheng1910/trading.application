package com.example.trading.application.repository;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;


@Service
public class CurrencyPairRepositoryFactory implements ApplicationContextAware {
  private ApplicationContext applicationContext;

  public Object getRepository(String currencyPair) {
    return getApplicationContext().getBean(getRepositoryName(currencyPair));
  }

  private static String getRepositoryName(String currencyPair){
    return decapitalizeString(currencyPair + "PairRepository");
  }

  public static String decapitalizeString(String input) {
    return Character.toLowerCase(input.charAt(0)) +
            (input.length() > 1 ? input.substring(1) : "");
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  public ApplicationContext getApplicationContext() {
    return applicationContext;
  }
}
