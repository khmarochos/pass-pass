package ua.tucha.passpass.web.router.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Slf4j
@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ViewRegistry {

    @XmlElement(name = "route")
    private List<Route> routeList;

}
