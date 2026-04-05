package co.edu.unipiloto.ws.agendaws.services;

import co.edu.unipiloto.ws.agendaws.model.Persona;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

/**
 * PersonaService — Clase de servicios REST para la agenda.
 *
 * URL base: /webresources/service
 *
 * Endpoints disponibles:
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ GET  /getAllPersonsInXml          → Lista de personas en XML            │
 * │ GET  /getAllPersonsInJson         → Lista de personas en JSON           │
 * │ GET  /getPersonByIdXML/{id}       → Persona por ID en XML              │
 * │ GET  /getPersonByIdJson/{id}      → Persona por ID en JSON             │
 * │ GET  /getAverageSalaryInXML       → Salario promedio en XML  (NUEVO)   │
 * │ GET  /getSumSalariesInJson        → Suma de salarios en JSON (NUEVO)   │
 * │ POST /addPersonInJson             → Agregar persona (body JSON)        │
 * └─────────────────────────────────────────────────────────────────────────┘
 */
@Path("service")
public class PersonaService {

    // -------------------------------------------------------
    // Almacén en memoria (HashMap estático = persiste en la
    // misma sesión del servidor, suficiente para demo/lab)
    // -------------------------------------------------------
    private static final Map<Integer, Persona> personas = new HashMap<>();

    // -------------------------------------------------------
    // Bloque estático: carga 10 personas de prueba al arrancar
    // -------------------------------------------------------
    static {
        Random rnd = new Random();
        for (int i = 1; i <= 10; i++) {
            int edad = rnd.nextInt(40) + 18;   // entre 18 y 57 años
            personas.put(i, new Persona(i, "My wonderfull Person " + i, edad));
        }
    }

    // ===========================================================
    // ENDPOINTS GET — Consultas
    // ===========================================================

    /**
     * Retorna todas las personas en formato XML.
     * URL: GET /webresources/service/getAllPersonsInXml
     */
    @GET
    @Path("/getAllPersonsInXml")
    @Produces(MediaType.APPLICATION_XML)
    public List<Persona> getAllPersonsInXml() {
        return new ArrayList<>(personas.values());
    }

    /**
     * Retorna todas las personas en formato JSON.
     * URL: GET /webresources/service/getAllPersonsInJson
     */
    @GET
    @Path("/getAllPersonsInJson")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Persona> getAllPersonsInJson() {
        return new ArrayList<>(personas.values());
    }

    /**
     * Retorna una persona por su ID en formato XML.
     * URL: GET /webresources/service/getPersonByIdXML/{id}
     */
    @GET
    @Path("/getPersonByIdXML/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Persona getPersonByIdXML(@PathParam("id") int id) {
        return personas.get(id);
    }

    /**
     * Retorna una persona por su ID en formato JSON.
     * URL: GET /webresources/service/getPersonByIdJson/{id}
     */
    @GET
    @Path("/getPersonByIdJson/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Persona getPersonByIdJson(@PathParam("id") int id) {
        return personas.get(id);
    }

    // ===========================================================
    // ENDPOINT NUEVO 1 — Salario promedio en XML
    // ===========================================================

    /**
     * Calcula el salario promedio de todas las personas y lo retorna en XML.
     * URL: GET /webresources/service/getAverageSalaryInXML
     *
     * Respuesta XML de ejemplo:
     * <company>
     *   <averageSalary>1234567.89</averageSalary>
     *   <totalPersons>10</totalPersons>
     * </company>
     */
    @GET
    @Path("/getAverageSalaryInXML")
    @Produces(MediaType.APPLICATION_XML)
    public Company getAverageSalaryInXML() {
        double total = 0;
        for (Persona p : personas.values()) {
            total += p.getSalary();
        }
        double promedio = personas.isEmpty() ? 0 : total / personas.size();

        Company result = new Company();
        result.setAverageSalary(Math.round(promedio * 100.0) / 100.0);
        result.setTotalPersons(personas.size());
        return result;
    }

    // ===========================================================
    // ENDPOINT NUEVO 2 — Suma de salarios en JSON
    // ===========================================================

    /**
     * Calcula la suma total de salarios y la retorna en JSON.
     * URL: GET /webresources/service/getSumSalariesInJson
     *
     * Respuesta JSON de ejemplo:
     * { "totalSalary": 9876543.21, "totalPersons": 10 }
     */
    @GET
    @Path("/getSumSalariesInJson")
    @Produces(MediaType.APPLICATION_JSON)
    public SalaryResult getSumSalariesInJson() {
        double total = 0;
        for (Persona p : personas.values()) {
            total += p.getSalary();
        }

        SalaryResult result = new SalaryResult();
        result.setTotalSalary(Math.round(total * 100.0) / 100.0);
        result.setTotalPersons(personas.size());
        return result;
    }

    // ===========================================================
    // ENDPOINT POST — Agregar persona
    // ===========================================================

    /**
     * Agrega una nueva persona recibida como JSON en el body.
     * URL: POST /webresources/service/addPersonInJson
     * Content-Type: application/json
     * Body: { "id": 11, "fullName": "Nueva Persona", "age": 30 }
     */
    @POST
    @Path("/addPersonInJson")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Persona addPersonInJson(Persona persona) {
        System.out.println("Agregando persona: " + persona);
        // Recalcular salario por si viene del cliente sin él
        persona.setAge(persona.getAge());
        personas.put(persona.getId(), persona);
        return persona;
    }

    // ===========================================================
    // Clases internas de respuesta (wrappers para XML/JSON)
    // ===========================================================

    /**
     * Wrapper para la respuesta del salario promedio (XML).
     */
    @javax.xml.bind.annotation.XmlRootElement(name = "company")
    @javax.xml.bind.annotation.XmlType(propOrder = {"averageSalary", "totalPersons"})
    public static class Company {

        private double averageSalary;
        private int    totalPersons;

        @javax.xml.bind.annotation.XmlElement
        public double getAverageSalary() { return averageSalary; }
        public void setAverageSalary(double averageSalary) { this.averageSalary = averageSalary; }

        @javax.xml.bind.annotation.XmlElement
        public int getTotalPersons() { return totalPersons; }
        public void setTotalPersons(int totalPersons) { this.totalPersons = totalPersons; }
    }

    /**
     * Wrapper para la respuesta de la suma de salarios (JSON).
     */
    @javax.xml.bind.annotation.XmlRootElement
    public static class SalaryResult {

        private double totalSalary;
        private int    totalPersons;

        public double getTotalSalary() { return totalSalary; }
        public void setTotalSalary(double totalSalary) { this.totalSalary = totalSalary; }

        public int getTotalPersons() { return totalPersons; }
        public void setTotalPersons(int totalPersons) { this.totalPersons = totalPersons; }
    }
}
