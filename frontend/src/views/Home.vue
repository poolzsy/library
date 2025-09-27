<template>
    <div class="home-container">
        <!-- 欢迎语 -->
        <el-row>
            <el-col>
                <div class="welcome-header">
                    <h2 class="main-title">欢迎回来, Admin!</h2>
                    <p class="sub-title">开始新的一天，管理您的图书系统吧。</p>
                </div>
            </el-col>
        </el-row>

        <!-- 核心数据统计 -->
        <el-row :gutter="20">
            <el-col :span="6">
                <el-card class="stat-card" shadow="hover">
                    <div class="card-content">
                        <div class="icon-wrapper" style="color: #409EFF;">
                            <el-icon>
                                <User />
                            </el-icon>
                        </div>
                        <div class="text-wrapper">
                            <p class="stat-value">1,280</p>
                            <p class="stat-label">总用户数</p>
                        </div>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card class="stat-card" shadow="hover">
                    <div class="card-content">
                        <div class="icon-wrapper" style="color: #67C23A;">
                            <el-icon>
                                <Collection />
                            </el-icon>
                        </div>
                        <div class="text-wrapper">
                            <p class="stat-value">8,543</p>
                            <p class="stat-label">图书总量</p>
                        </div>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card class="stat-card" shadow="hover">
                    <div class="card-content">
                        <div class="icon-wrapper" style="color: #E6A23C;">
                            <el-icon>
                                <Reading />
                            </el-icon>
                        </div>
                        <div class="text-wrapper">
                            <p class="stat-value">926</p>
                            <p class="stat-label">今日借阅</p>
                        </div>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card class="stat-card" shadow="hover">
                    <div class="card-content">
                        <div class="icon-wrapper" style="color: #F56C6C;">
                            <el-icon>
                                <DocumentAdd />
                            </el-icon>
                        </div>
                        <div class="text-wrapper">
                            <p class="stat-value">128</p>
                            <p class="stat-label">本周新增</p>
                        </div>
                    </div>
                </el-card>
            </el-col>
        </el-row>

        <!-- 数据图表 -->
        <el-row :gutter="20" style="margin-top: 20px;">
            <el-col :span="16">
                <el-card shadow="never">
                    <template #header>
                        <div>每月新增图书趋势</div>
                    </template>
                    <!-- 柱状图容器 -->
                    <div ref="barChartRef" class="chart-container"></div>
                </el-card>
            </el-col>
            <el-col :span="8">
                <el-card shadow="never">
                    <template #header>
                        <div>图书分类占比</div>
                    </template>
                    <!-- 饼图容器 -->
                    <div ref="pieChartRef" class="chart-container"></div>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { User, Collection, Reading, DocumentAdd } from '@element-plus/icons-vue'
import * as echarts from 'echarts';

// 创建 DOM 引用
const barChartRef = ref(null);
const pieChartRef = ref(null);

const initCharts = () => {
    // 柱状图实例和配置
    const barChart = echarts.init(barChartRef.value);
    const barOption = {
        tooltip: { trigger: 'axis' },
        xAxis: {
            type: 'category',
            data: ['一月', '二月', '三月', '四月', '五月', '六月']
        },
        yAxis: { type: 'value' },
        series: [{
            name: '新增图书',
            type: 'bar',
            data: [120, 200, 150, 80, 70, 110],
            itemStyle: { color: '#409EFF' }
        }],
        grid: { top: '15%', left: '12%', right: '5%', bottom: '15%' }
    };
    barChart.setOption(barOption);

    // 饼图实例和配置
    const pieChart = echarts.init(pieChartRef.value);
    const pieOption = {
        tooltip: { trigger: 'item' },
        legend: { top: 'bottom', left: 'center' },
        series: [{
            name: '图书分类',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            label: { show: false, position: 'center' },
            emphasis: { label: { show: true, fontSize: '20', fontWeight: 'bold' } },
            labelLine: { show: false },
            data: [
                { value: 1048, name: '文学' },
                { value: 735, name: '科技' },
                { value: 580, name: '历史' },
                { value: 484, name: '艺术' },
                { value: 300, name: '教育' }
            ]
        }]
    };
    pieChart.setOption(pieOption);

    window.addEventListener('resize', () => {
        barChart.resize();
        pieChart.resize();
    });
};

onMounted(() => {
    nextTick(() => {
        initCharts();
    });
});
</script>

<style scoped>
@import '@/assets/css/Home.css';
</style>
