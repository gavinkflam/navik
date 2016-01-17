package hk.gavin.navik.injection;

import javax.inject.Scope;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Scope @Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {
    
}
