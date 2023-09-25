package com.pm.employeemanagement.repository;

import com.pm.employeemanagement.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class EmployeeCustomRepositoryImpl implements EmployeeCustomRepository {

    @PersistenceContext
    EntityManager entityManager;
    @Override
    public List<Employee> getEmployeeByEmailAndDepartment(String email,String department,int pageNumber,int pageSize) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> employee = cq.from(Employee.class);
        List<Predicate> predicates = new ArrayList<>();

        if(email != null && !email.isEmpty()){

            predicates.add(cb.equal(employee.get("email"), email));
        }

        if(department != null && !department.isEmpty()){

            predicates.add(cb.equal(employee.get("department"), department));
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        TypedQuery<Employee> query = entityManager.createQuery(cq);
        //pagination
        int offset  = (pageNumber-1)*pageSize;
        query.setFirstResult(offset);
        query.setMaxResults(pageSize);

        return query.getResultList();



    }

    @Override
    public long countByEmailAndDepartment(String email, String department) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Employee> root = query.from(Employee.class);

        Predicate predicate = cb.conjunction();

        if (email != null && !email.isEmpty()) {
            predicate = cb.and(predicate, cb.equal(root.get("email"), email));
        }
        if (department != null && !department.isEmpty()) {
            predicate = cb.and(predicate, cb.equal(root.get("department"), department));
        }

        query.select(cb.count(root)).where(predicate);

        return entityManager.createQuery(query).getSingleResult();
    }


}
