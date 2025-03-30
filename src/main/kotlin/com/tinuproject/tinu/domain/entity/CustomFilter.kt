package com.tinuproject.tinu.domain.entity


import com.tinuproject.tinu.domain.customfilter.dto.client_controller.request.UpdateCustomFilter
import com.tinuproject.tinu.domain.entity.base.BaseEntity
import jakarta.persistence.*

@Entity
class CustomFilter (

    @Column
    var filterName : String,

    @Column
    var maxPrice : Int?,

    @Column
    var minPrice :Int?,

    @Column
    var onlySell : Boolean?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    var member : Member,

    @OneToMany(mappedBy = "customFilter", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    var customCategory: MutableList<CustomCategory> = mutableListOf()
) : BaseEntity(){

    fun updateCustomFilter(updateCustomFilter: UpdateCustomFilter){
        this.filterName = updateCustomFilter.filterName
        this.onlySell = updateCustomFilter.onlySell
        this.maxPrice = updateCustomFilter.maxPrice
        this.minPrice = updateCustomFilter.minPrice
    }
}