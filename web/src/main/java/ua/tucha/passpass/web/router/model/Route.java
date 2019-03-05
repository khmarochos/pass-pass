package ua.tucha.passpass.web.router.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Slf4j
@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"name", "path", "view", "routeList"})
public class Route {

    @XmlAttribute
    private String name;

    @XmlAttribute
    private String path;

    @XmlAttribute
    private String view;

    @XmlElement(name = "route")
    List<Route> routeList;

}
