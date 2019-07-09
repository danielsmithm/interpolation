module br.ufrn.interpolation.web {
    exports br.ufrn.interpolation.web.configuration;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.context;
    requires spring.web;
    requires br.ufrn.interpolation.core;
    requires br.ufrn.interpolation.domain;
    requires spring.beans;
    requires br.ufrn.interpolation.infrastructure;

}
