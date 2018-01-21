package com.nunez.palcine.framework.respository.data

class TheaterRaw(
        val name: String,
        val halls: List<HallRaw>)

class HallRaw(
        val name: String,
        val schedules: List<ScheduleRaw>)

class ScheduleRaw(
        val day: String,
        val time: List<String>)