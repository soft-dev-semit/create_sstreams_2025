$(document).ready(function() {
    $('.showGroups').click(function(event) {
        $('.table5').addClass('d-none');
        $('.table4').addClass('d-none');
        $('.table3').addClass('d-none');
        $('.table2').addClass('d-none');
        $('.table1').removeClass('d-none');
    });
    $('.showPlansSpring').click(function(event) {
        $('.table5').addClass('d-none');
        $('.table4').addClass('d-none');
        $('.table3').addClass('d-none');
        $('.table2').removeClass('d-none');
        $('.table1').addClass('d-none');
    });
    $('.showPlansAutumn').click(function(event) {
        $('.table5').addClass('d-none');
        $('.table4').addClass('d-none');
        $('.table3').removeClass('d-none');
        $('.table2').addClass('d-none');
        $('.table1').addClass('d-none');
    });
    $('.createStreamsAutumn').click(function(event) {
        $('.table5').addClass('d-none');
        $('.table4').removeClass('d-none');
        $('.table3').addClass('d-none');
        $('.table2').addClass('d-none');
        $('.table1').addClass('d-none');
    });
    $('.createStreamsSpring').click(function(event) {
        $('.table5').removeClass('d-none');
        $('.table4').addClass('d-none');
        $('.table3').addClass('d-none');
        $('.table2').addClass('d-none');
        $('.table1').addClass('d-none');
    });
    if ($('#errorAlert').length > 0) {
        // Автоматическое скрытие сообщения об ошибке через 5 секунд
        setTimeout(function() {
            $('#errorAlert').alert('close');
        }, 5000);
    }
    $('.upload-form').on('submit', function() {
        $('#loadingModal').modal('show');
    });
    $('.create-stream-form').on('submit', function() {
        $('#loadingModal2').modal('show');
    });

    // Получаем текущий год (последние 2 цифры)
    const currentYear = new Date().getFullYear() % 100;

    // Функция извлечения кода специальности из строки вида Lec_122_420a
    function extractSpecialtyCode(streamName) {
        const parts = streamName.split('_');
        return parts.length > 1 ? parts[1] : '';
    }

    // Функция проверки, является ли код специальности одним из основных
    function isMainSpecialtyCode(code) {
        return ['121', '122', '126'].includes(code);
    }

    // Функция определения курса из групп в потоке
    function determineStreamCourse(groupsString) {
        const groups = groupsString.split(', ');
        let courses = [];

        for (let group of groups) {
            // Ищем шифр группы с цифрами
            const match = group.match(/[А-Яа-яA-Za-z]+[-](\d{2,3})/);
            if (match) {
                let groupCode = match[1];
                // Если код состоит из 3 цифр, берем последние 2
                if (groupCode.length === 3) {
                    groupCode = groupCode.substring(1, 3);
                }
                let groupYear = parseInt(groupCode);
                let course = currentYear - groupYear;

                // Если группа содержит ".с" (с кириллической "с") или "c" (с латинской "c"), добавляем 1 к курсу
                if (group.match(/\d+(\.?[сc])/i)) {
                    course += 1;
                }
                // Курс не может быть отрицательным или нулевым
                if (course > 0) {
                    courses.push(course);
                }
            }
        }

        // Если есть хотя бы один определенный курс, возвращаем уникальные курсы
        return [...new Set(courses)];
    }

    // Добавляем опции для фильтра по курсу при загрузке страницы
    function populateCoursesFilter() {
        // Для осенни
        let autumnCourses = new Set();
        $('.table4 .table tr:not(:first-child)').each(function() {
            const groupsCell = $(this).find('td:eq(2)').text();
            const courses = determineStreamCourse(groupsCell);
            courses.forEach(course => autumnCourses.add(course));
        });

        // Для весны
        let springCourses = new Set();
        $('.table5 .table tr:not(:first-child)').each(function() {
            const groupsCell = $(this).find('td:eq(2)').text();
            const courses = determineStreamCourse(groupsCell);
            courses.forEach(course => springCourses.add(course));
        });

        // Заполняем выпадающие списки
        const autumnCoursesList = [...autumnCourses].sort((a, b) => a - b);
        const springCoursesList = [...springCourses].sort((a, b) => a - b);

        let autumnHtml = '<option value="">Всі курси</option>';
        autumnCoursesList.forEach(course => {
            autumnHtml += `<option value="${course}">${course} курс</option>`;
        });
        $('#courseFilter').html(autumnHtml);

        let springHtml = '<option value="">Всі курси</option>';
        springCoursesList.forEach(course => {
            springHtml += `<option value="${course}">${course} курс</option>`;
        });
        $('#courseFilterSpring').html(springHtml);
    }

    function applyFiltersAutumn() {
        const selectedGroups = $('#groupFilter').val();
        const selectedType = $('#streamTypeFilter').val();
        const studentType = $('#studentTypeFilter').val();
        const specialtyCode = $('#specialtyCodeFilter').val();
        const selectedCourse = $('#courseFilter').val();

        $('.table4 .table tr:not(:first-child)').each(function() {
            let showRow = true;

            // Фильтр по коду специальности
            if (specialtyCode && showRow) {
                const streamNameCell = $(this).find('td:eq(0)').text();
                const rowSpecialtyCode = extractSpecialtyCode(streamNameCell);

                if (specialtyCode === 'other') {
                    // Если выбрано "Інші", показываем все, кроме 121, 122, 126
                    if (isMainSpecialtyCode(rowSpecialtyCode)) {
                        showRow = false;
                    }
                } else if (rowSpecialtyCode !== specialtyCode) {
                    showRow = false;
                }
            }

            // Фильтр по группам
            if (selectedGroups && showRow) {
                const groupsCell = $(this).find('td:eq(2)').text();
                if (!groupsCell.includes(selectedGroups)) showRow = false;
            }

            // Фильтр по типу потока (Lec, Lab, Prak, KR)
            if (selectedType && showRow) {
                const streamNameCell = $(this).find('td:eq(0)').text();
                if (!streamNameCell.startsWith(selectedType + '_')) showRow = false;
            }

            // Фильтр по типу студентов (иностранцы/украинцы)
            if (studentType && showRow) {
                const groupsCell = $(this).find('td:eq(2)').text();
                const groupsList = groupsCell.split(', ');

                if (studentType === 'foreign') {
                    // Проверяем наличие иностранных групп (с суффиксом 'e')
                    const hasForeignGroups = groupsList.some(group => group.trim().endsWith('е'));
                    if (!hasForeignGroups) showRow = false;
                } else if (studentType === 'ukrainian') {
                    // Проверяем наличие украинских групп (без суффикса 'e')
                    const hasUkrainianGroups = groupsList.some(group => !group.trim().endsWith('е'));
                    if (!hasUkrainianGroups) showRow = false;
                }
            }

            // Фильтр по курсу
            if (selectedCourse && showRow) {
                const groupsCell = $(this).find('td:eq(2)').text();
                const courses = determineStreamCourse(groupsCell);
                if (!courses.includes(parseInt(selectedCourse))) {
                    showRow = false;
                }
            }

            // Показать или скрыть строку
            $(this).toggle(showRow);
        });
    }

    function applyFiltersSpring() {
        const selectedGroups = $('#groupFilterSpring').val();
        const selectedType = $('#streamTypeFilterSpring').val();
        const studentType = $('#studentTypeFilterSpring').val();
        const specialtyCode = $('#specialtyCodeFilterSpring').val();
        const selectedCourse = $('#courseFilterSpring').val();

        $('.table5 .table tr:not(:first-child)').each(function() {
            let showRow = true;

            // Фильтр по коду специальности
            if (specialtyCode && showRow) {
                const streamNameCell = $(this).find('td:eq(0)').text();
                const rowSpecialtyCode = extractSpecialtyCode(streamNameCell);

                if (specialtyCode === 'other') {
                    // Если выбрано "Інші", показываем все, кроме 121, 122, 126
                    if (isMainSpecialtyCode(rowSpecialtyCode)) {
                        showRow = false;
                    }
                } else if (rowSpecialtyCode !== specialtyCode) {
                    showRow = false;
                }
            }

            // Фильтр по группам
            if (selectedGroups && showRow) {
                const groupsCell = $(this).find('td:eq(2)').text();
                if (!groupsCell.includes(selectedGroups)) showRow = false;
            }

            // Фильтр по типу потока (Lec, Lab, Prak, KR)
            if (selectedType && showRow) {
                const streamNameCell = $(this).find('td:eq(0)').text();
                if (!streamNameCell.startsWith(selectedType + '_')) showRow = false;
            }

            // Фильтр по типу студентов (иностранцы/украинцы)
            if (studentType && showRow) {
                const groupsCell = $(this).find('td:eq(2)').text();
                const groupsList = groupsCell.split(', ');

                if (studentType === 'foreign') {
                    // Проверяем наличие иностранных групп (с суффиксом 'e')
                    const hasForeignGroups = groupsList.some(group => group.trim().endsWith('е'));
                    if (!hasForeignGroups) showRow = false;
                } else if (studentType === 'ukrainian') {
                    // Проверяем наличие украинских групп (без суффикса 'e')
                    const hasUkrainianGroups = groupsList.some(group => !group.trim().endsWith('е'));
                    if (!hasUkrainianGroups) showRow = false;
                }
            }

            // Фильтр по курсу
            if (selectedCourse && showRow) {
                const groupsCell = $(this).find('td:eq(2)').text();
                const courses = determineStreamCourse(groupsCell);
                if (!courses.includes(parseInt(selectedCourse))) {
                    showRow = false;
                }
            }

            // Показать или скрыть строку
            $(this).toggle(showRow);
        });
    }

    // Привязка событий к фильтрам
    $('#groupFilter, #streamTypeFilter, #studentTypeFilter, #specialtyCodeFilter, #courseFilter').on('change', applyFiltersAutumn);
    $('#groupFilterSpring, #streamTypeFilterSpring, #studentTypeFilterSpring, #specialtyCodeFilterSpring, #courseFilterSpring').on('change', applyFiltersSpring);

    // Заполняем фильтры по курсам при загрузке страницы
    populateCoursesFilter();
});