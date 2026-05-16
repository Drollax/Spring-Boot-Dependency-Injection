package com.workintech.s17d2.tax;

import org.springframework.stereotype.Component;


public interface Taxable {
   Double getSimpleTaxRate();
   Double getMiddleTaxRate();
   Double getUpperTaxRate();
}
