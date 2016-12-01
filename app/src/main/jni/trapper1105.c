/*
 * MATLAB Compiler: 4.2 (R14SP2)
 * Date: Mon Nov 03 10:48:39 2014
 * Arguments: "-B" "macro_default" "-t" "-l" "trapper1105.m" 
 */

#include <stdio.h>
#define EXPORTING_trapper1105 1
#include "trapper1105.h"
#ifdef __cplusplus
extern "C" {
#endif
extern const unsigned char __MCC_trapper1105_public_data[];
extern const char *__MCC_trapper1105_name_data;
extern const char *__MCC_trapper1105_root_data;
extern const unsigned char __MCC_trapper1105_session_data[];
extern const char *__MCC_trapper1105_matlabpath_data[];
extern const int __MCC_trapper1105_matlabpath_data_count;
extern const char *__MCC_trapper1105_classpath_data[];
extern const int __MCC_trapper1105_classpath_data_count;
extern const char *__MCC_trapper1105_lib_path_data[];
extern const int __MCC_trapper1105_lib_path_data_count;
extern const char *__MCC_trapper1105_mcr_runtime_options[];
extern const int __MCC_trapper1105_mcr_runtime_option_count;
extern const char *__MCC_trapper1105_mcr_application_options[];
extern const int __MCC_trapper1105_mcr_application_option_count;
#ifdef __cplusplus
}
#endif


static HMCRINSTANCE _mcr_inst = NULL;


#if defined( _MSC_VER) || defined(__BORLANDC__) || defined(__WATCOMC__) || defined(__LCC__)
#include <windows.h>

static char path_to_dll[_MAX_PATH];

BOOL WINAPI DllMain(HINSTANCE hInstance, DWORD dwReason, void *pv)
{
    if (dwReason == DLL_PROCESS_ATTACH)
    {
        char szDllPath[_MAX_PATH];
        char szDir[_MAX_DIR];
        if (GetModuleFileName(hInstance, szDllPath, _MAX_PATH) > 0)
        {
             _splitpath(szDllPath, path_to_dll, szDir, NULL, NULL);
            strcat(path_to_dll, szDir);
        }
	else return FALSE;
    }
    else if (dwReason == DLL_PROCESS_DETACH)
    {
    }
    return TRUE;
}
#endif
static int mclDefaultPrintHandler(const char *s)
{
    return fwrite(s, sizeof(char), strlen(s), stdout);
}

static int mclDefaultErrorHandler(const char *s)
{
    int written = 0, len = 0;
    len = strlen(s);
    written = fwrite(s, sizeof(char), len, stderr);
    if (len > 0 && s[ len-1 ] != '\n')
        written += fwrite("\n", sizeof(char), 1, stderr);
    return written;
}


/* This symbol is defined in shared libraries. Define it here
 * (to nothing) in case this isn't a shared library. 
 */
#ifndef LIB_trapper1105_C_API 
#define LIB_trapper1105_C_API /* No special import/export declaration */
#endif

LIB_trapper1105_C_API 
bool trapper1105InitializeWithHandlers(
    mclOutputHandlerFcn error_handler,
    mclOutputHandlerFcn print_handler
)
{
    if (_mcr_inst != NULL)
        return true;
    if (!mclmcrInitialize())
        return false;
    if (!mclInitializeComponentInstance(&_mcr_inst,
                                        __MCC_trapper1105_public_data,
                                        __MCC_trapper1105_name_data,
                                        __MCC_trapper1105_root_data,
                                        __MCC_trapper1105_session_data,
                                        __MCC_trapper1105_matlabpath_data,
                                        __MCC_trapper1105_matlabpath_data_count,
                                        __MCC_trapper1105_classpath_data,
                                        __MCC_trapper1105_classpath_data_count,
                                        __MCC_trapper1105_lib_path_data,
                                        __MCC_trapper1105_lib_path_data_count,
                                        __MCC_trapper1105_mcr_runtime_options,
                                        __MCC_trapper1105_mcr_runtime_option_count,
                                        true, NoObjectType, LibTarget,
                                        path_to_dll, error_handler,
                                        print_handler))
        return false;
    return true;
}

LIB_trapper1105_C_API 
bool trapper1105Initialize(void)
{
    return trapper1105InitializeWithHandlers(mclDefaultErrorHandler,
                                             mclDefaultPrintHandler);
}

LIB_trapper1105_C_API 
void trapper1105Terminate(void)
{
    if (_mcr_inst != NULL)
        mclTerminateInstance(&_mcr_inst);
}


LIB_trapper1105_C_API 
void mlxTrapper1105(int nlhs, mxArray *plhs[], int nrhs, mxArray *prhs[])
{
    mclFeval(_mcr_inst, "trapper1105", nlhs, plhs, nrhs, prhs);
}

LIB_trapper1105_C_API 
void mlfTrapper1105(int nargout, mxArray** y, mxArray* x, mxArray* f1
                    , mxArray* f3, mxArray* fsl, mxArray* fsh
                    , mxArray* rp, mxArray* rs, mxArray* Fs)
{
    mclMlfFeval(_mcr_inst, "trapper1105", nargout, 1,
                8, y, x, f1, f3, fsl, fsh, rp, rs, Fs);
}
