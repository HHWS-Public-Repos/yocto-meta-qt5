SUMMARY = "Python Qt5 Bindings"
AUTHOR = "Phil Thomson @ riverbank.co.uk"
HOMEPAGE = "https://www.riverbankcomputing.com/software/pyqt"
SECTION = "devel/python"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "\
    file://LICENSE;md5=d32239bcb673463ab874e80d47fae504 \
"

inherit pypi

PYPI_PACKAGE = "PyQt5"

SRC_URI[md5sum] = "b60f895e70d31d774e6cd374efd17c65"
SRC_URI[sha256sum] = "d9a76b850246d08da9863189ecb98f6c2aa9b4d97a3e85e29330a264aed0f9a1"

S = "${WORKDIR}/PyQt5-${PV}"

inherit qmake5
inherit python3native python3-dir

DEPENDS = "qtbase qtdeclarative qtquickcontrols2"
DEPENDS += "sip3 sip3-native python3"

CONFIGURE_SYSROOT = "${STAGING_DIR_HOST}"
CONFIGURE_SYSROOT_class-native = "${STAGING_DIR_NATIVE}"

export BUILD_SYS
export HOST_SYS
export STAGING_INCDIR
export STAGING_LIBDIR

PARALLEL_MAKEINST = ""

DISABLED_FEATURES = "PyQt_Desktop_OpenGL PyQt_Accessibility PyQt_SessionManager ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', '', 'PyQt_OpenGL', d)}"

PYQT_MODULES = "QtCore QtGui QtNetwork QtXml QtNetwork QtQml ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'QtQuick QtWidgets QtQuickWidgets', '', d)}"
PYQT_MODULES_class-native = "QtCore"

BBCLASSEXTEND = "native"

do_configure_prepend() {
    cd ${S}
    echo "py_platform = linux" > pyqt.cfg
    echo "py_inc_dir = ${STAGING_INCDIR}/python%(py_major).%(py_minor)${PYTHON_ABI}" >> pyqt.cfg
    echo "py_pylib_dir = ${STAGING_LIBDIR}/python%(py_major).%(py_minor)" >> pyqt.cfg
    echo "py_pylib_lib = python$%(py_major).%(py_minor)" >> pyqt.cfg
    echo "pyqt_module_dir = ${D}/${libdir}/python%(py_major).%(py_minor)/site-packages" >> pyqt.cfg
    echo "pyqt_bin_dir = ${D}/${bindir}" >> pyqt.cfg
    echo "pyqt_sip_dir = ${D}/${datadir}/sip/PyQt5" >> pyqt.cfg
    echo "pyuic_interpreter = ${D}/${bindir}/python%(py_major).%(py_minor)" >> pyqt.cfg
    echo "pyqt_disabled_features = ${DISABLED_FEATURES}" >> pyqt.cfg
    echo "qt_shared = True" >> pyqt.cfg
    QT_VERSION=`${OE_QMAKE_QMAKE} -query QT_VERSION`
    echo "[Qt $QT_VERSION]" >> pyqt.cfg
    echo "pyqt_modules = ${PYQT_MODULES}" >> pyqt.cfg
    echo yes | ${PYTHON} configure.py --verbose --qmake  ${STAGING_BINDIR_NATIVE}/${QT_DIR_NAME}/qmake --configuration pyqt.cfg --sysroot ${CONFIGURE_SYSROOT}
}

do_compile() {
    cd ${S}
    oe_runmake
}

# Can't merely override do_install() when supporting native builds,
# because qmake5 class provides a stronger override by defining
# do_install_class-native().
#
# Thus define a common install function that can be run from
# both do_install() and do_install_class-native().
custom_install() {
    cd ${S}
    oe_runmake MAKEFLAGS='-j 1' install

    # path of python during build gets hard-coded into scripts,
    # strip off directory to let it be found on PATH
    for SCRIPT in ${D}${bindir}/* ; do
        sed -i "s:exec /.*python:python:" $SCRIPT
    done
}

do_install() {
    custom_install
}

do_install_class-native() {
    custom_install
}

FILES_${PN} += "${libdir}/${PYTHON_DIR}/site-packages ${datadir}/sip/PyQt5/"

RDEPENDS_${PN} = "qtbase qtdeclarative qtquickcontrols2 qtquickcontrols2-mkspecs"
RDEPENDS_${PN} += "python3-core python3-sip3"
